package com.plexonic.retention.projection;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public interface UserProjection {
    String getId();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate getInstallDate();
}
