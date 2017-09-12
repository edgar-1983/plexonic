package com.plexonic.retention.controller;

import com.google.common.collect.ImmutableMap;
import com.plexonic.retention.Application;
import com.plexonic.retention.model.Request;
import com.plexonic.retention.model.User;
import com.plexonic.retention.repository.RequestRepository;
import com.plexonic.retention.repository.UserRepository;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.*;
import java.util.Collections;
import java.util.Date;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class AnalyticsControllerFunctionalTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    private MockMvc mockMvc;
    private Request request;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        User user = userRepository.save(User.builder().id("123").installDate(LocalDateTime.now()).build());
        request = Request.builder().requestDate(LocalDateTime.now()).user(user).build();
        requestRepository.save(request);
    }

    @After
    public void tearDown() throws Exception {
        requestRepository.deleteAll();
    }

    @Test
    public void testGetDau() throws Exception {
        String requestDate = request.getRequestDate().toLocalDate().toString();
        String payload = "{\n" +
                "\"dates\": [\"" + requestDate + "\"]\n" +
                "}";
        mockMvc.perform(put("/analytics/dau")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andDo(print())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value("123"))
                .andExpect(jsonPath("$.content[0].installDate").exists())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRetention() throws Exception {
        String payload = "{\n" +
                "\"startDate\": \"2017-06-22\",\n" +
                "\"day\": \"Day8\"\n" +
                "}";
        mockMvc.perform(put("/analytics/retention")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRetentionInValidDay() throws Exception {
        String payload = "{\n" +
                "\"startDate\": \"2017-06-22\",\n" +
                "\"day\": \"DayTwo\"\n" +
                "}";
        mockMvc.perform(put("/analytics/retention")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(jsonPath("$.message").value("day must be an integer"))
                .andExpect(status().isBadRequest());
    }
}
