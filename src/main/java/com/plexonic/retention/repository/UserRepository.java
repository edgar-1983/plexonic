package com.plexonic.retention.repository;

import com.plexonic.retention.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
