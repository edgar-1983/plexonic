package com.plexonic.retention.repository;


import com.plexonic.retention.model.Request;
import com.plexonic.retention.model.User;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long>,
        QueryDslPredicateExecutor<Request> {
    @Override
    List<Request> findAll(Predicate predicate);
}
