package com.plexonic.retention.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.plexonic.retention.binding.DauPayload;
import com.plexonic.retention.binding.RetentionPayload;
import com.plexonic.retention.handler.InvalidRequestException;
import com.plexonic.retention.projection.UserProjection;
import com.plexonic.retention.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("analytics")
public class AnalyticsController {
    private final RequestService requestService;
    private final ProjectionFactory projectionFactory;

    @Autowired
    public AnalyticsController(RequestService requestService, ProjectionFactory projectionFactory) {
        this.requestService = requestService;
        this.projectionFactory = projectionFactory;
    }

    @PutMapping("/dau")
    public Page<UserProjection> getDau(@RequestBody @Valid DauPayload payload,
                                       BindingResult bindingResult,
                                       Pageable pageable) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Validation issues", bindingResult);
        }
        return requestService.getDau(payload.getDates(), pageable).map(user ->
                projectionFactory.createProjection(UserProjection.class, user));
    }

    @PutMapping("/retention")
    public BigDecimal getRetention(@RequestBody @Valid RetentionPayload retentionPayload,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Validation issues", bindingResult);
        }
        return requestService.getRetention(retentionPayload);
    }
}
