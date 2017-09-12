package com.plexonic.retention.binding;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RetentionPayload {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private Date startDate;

    @NotBlank
    private String day;

    public long getDay() {
        String dayNo = day.replaceFirst("Day", "");
        try {
            long day = Long.parseLong(dayNo);
            if (day < 0) {
                throw new IllegalArgumentException("day must be positive integer");
            }
            return day;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("day must be an integer");
        }
    }

    public LocalDateTime getStartDate() {
        return this.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
