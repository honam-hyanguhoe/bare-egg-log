package org.egglog.api.calendar.dto.response;

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
public class ScheduleListOutputSpec {

    private Long scheduleId;

    private String scheduleTitle;

    private String scheduleContent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


}
