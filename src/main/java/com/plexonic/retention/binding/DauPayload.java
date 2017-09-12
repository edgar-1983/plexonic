package com.plexonic.retention.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DauPayload {
    @NotEmpty
    @NotNull
    Set<Date> dates;

    public Set<LocalDateTime> getDates() {
        return dates.stream()
                .map(date -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .collect(toSet());
    }
}
