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
public class AlarmListOutputSpec {

    private Long alarmId;

    private LocalTime alarmTime;

    private int replayCnt;

    private int replayTime;

    private Boolean isAlarmOn;

    private String workTypeTitle;   //근무 제목

    private String workTypeColor;   //근무 색깔
}
