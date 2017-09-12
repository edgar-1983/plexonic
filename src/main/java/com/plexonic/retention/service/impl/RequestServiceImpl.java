package com.plexonic.retention.service.impl;

import com.plexonic.retention.binding.RetentionPayload;
import com.plexonic.retention.model.QRequest;
import com.plexonic.retention.model.Request;
import com.plexonic.retention.model.User;
import com.plexonic.retention.repository.RequestRepository;
import com.plexonic.retention.service.RequestService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.plexonic.retention.model.QRequest.request;
import static java.math.BigDecimal.valueOf;
import static java.util.stream.Collectors.toList;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Page<User> getDau(Set<LocalDateTime> requestDates, Pageable pageable) {
        List<Request> requests = requestRepository.findAll(buildPredicate(requestDates));

        if (requests.isEmpty()) {
            throw new NoSuchElementException("No active users found for specified dates");
        }

        List<User> users = requests.stream().map(Request::getUser).distinct().collect(toList());
        return new PageImpl<User>(users, pageable, users.size());
    }

    @Override
    public BigDecimal getRetention(RetentionPayload payload) {
        LocalDateTime startDate = payload.getStartDate();
        LocalDateTime endDate = startDate.plusDays(payload.getDay());

        //Fetch users registered before start date
        BooleanExpression predicateBefore = request.user.installDate.lt(startDate);
        //Fetch users registered before start date who have returned within specified days after start date
        BooleanExpression predicateAfter = predicateBefore.and(request.requestDate.between(startDate, endDate));

        long startUsers = requestRepository.findAll(predicateBefore).stream().map(Request::getUser).distinct().count();
        long remainingUsers = requestRepository.findAll(predicateAfter).stream().map(Request::getUser).distinct().count();
        if (startUsers == 0) {
            return BigDecimal.ZERO;
        }

        return valueOf(remainingUsers).divide(valueOf(startUsers), 4, RoundingMode.HALF_DOWN)
                .multiply(valueOf(100));
    }

    private BooleanExpression buildPredicate(Set<LocalDateTime> requestDates) {
        //Need to implement filtering this way since LocalDateTime#equals() compares both date and time
        //while in our case time of payload date is always UTC midnight and may not match with that in db.
        List<Integer> yearList = requestDates.stream().map(LocalDateTime::getYear).collect(toList());
        List<Integer> monthList = requestDates.stream().map(LocalDateTime::getMonthValue).collect(toList());
        List<Integer> dayList = requestDates.stream().map(LocalDateTime::getDayOfMonth).collect(toList());
        return request.requestDate.year().in(yearList)
                .and(request.requestDate.month().in(monthList)
                        .and(request.requestDate.dayOfMonth().in(dayList)));
    }
}
