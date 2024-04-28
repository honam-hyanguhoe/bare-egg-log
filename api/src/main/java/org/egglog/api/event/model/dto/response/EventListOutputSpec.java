package org.egglog.api.event.model.dto.response;

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
public class EventListOutputSpec {

    private Long eventId;

    private String eventTitle;

    private String eventContent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long calendarGroupId;

}
