package org.egglog.api.alarm.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class AlarmOutputSpec {

    private Long alarmId;

    private LocalTime alarmTime;

    private int alarmReplayCnt;

    private int alarmReplayTime;

    private Boolean isAlarmOn;

    private String workTypeTitle;

    private String workTypeColor;
}
