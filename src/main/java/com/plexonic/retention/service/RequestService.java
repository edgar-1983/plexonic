package com.plexonic.retention.service;


import com.plexonic.retention.binding.RetentionPayload;
import com.plexonic.retention.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public interface RequestService {
    Page<User> getDau(Set<LocalDateTime> requestDates, Pageable pageable);
    BigDecimal getRetention(RetentionPayload payload);
}
