package com.plexonic.retention.service.impl;

import com.plexonic.retention.binding.RetentionPayload;
import com.plexonic.retention.model.QRequest;
import com.plexonic.retention.model.Request;
import com.plexonic.retention.model.User;
import com.plexonic.retention.repository.RequestRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRetention() throws Exception {
        LocalDateTime startDate = LocalDateTime.MIN;
        RetentionPayload retentionPayload = mock(RetentionPayload.class);
        when(retentionPayload.getStartDate()).thenReturn(startDate);
        when(retentionPayload.getDay()).thenReturn(8L);

        List<Request> requestsBefore = Arrays.asList(Request.builder().user(mock(User.class)).build(),
                Request.builder().user(mock(User.class)).build(), Request.builder().user(mock(User.class)).build(),
                Request.builder().user(mock(User.class)).build(), Request.builder().user(mock(User.class)).build());

        List<Request> requestsAfter = Arrays.asList(Request.builder().user(mock(User.class)).build(),
                Request.builder().user(mock(User.class)).build(), Request.builder().user(mock(User.class)).build());

        BooleanExpression before = QRequest.request.user.installDate.lt(startDate);
        BooleanExpression after = before.and(QRequest.request.requestDate.between(startDate, startDate.plusDays(8L)));
        when(requestRepository.findAll(eq(before))).thenReturn(requestsBefore);
        when(requestRepository.findAll(eq(after))).thenReturn(requestsAfter);

        BigDecimal expected = valueOf(3).divide(valueOf(5), 4, RoundingMode.HALF_DOWN).multiply(valueOf(100));
        BigDecimal actual = requestService.getRetention(retentionPayload);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGeDau() throws Exception {
        List<Request> requests = Arrays.asList(Request.builder().user(mock(User.class)).build(),
                Request.builder().user(mock(User.class)).build(), Request.builder().user(mock(User.class)).build(),
                Request.builder().user(mock(User.class)).build(), Request.builder().user(mock(User.class)).build());

        when(requestRepository.findAll(any(Predicate.class))).thenReturn(requests);

        List<User> actual = requestService.getDau(Collections.singleton(LocalDateTime.now()), null).getContent();
        Assert.assertEquals(5, actual.size());
    }
}
