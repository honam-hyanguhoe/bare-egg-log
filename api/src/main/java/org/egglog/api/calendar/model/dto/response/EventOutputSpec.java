package org.egglog.api.calendar.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class EventOutputSpec {

    private Long eventId;

    private String eventTitle;

    private String eventContent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
