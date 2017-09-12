package com.plexonic.retention.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "plex_user")
@Entity
public class User {
    @Id
    @Column(name = "user_id")
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss Z")
    private LocalDateTime installDate;
}
