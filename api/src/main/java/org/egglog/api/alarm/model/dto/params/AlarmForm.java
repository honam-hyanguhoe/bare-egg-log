package org.egglog.api.alarm.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AlarmForm {

    private LocalTime alarmTime;

    private int replayCnt;

    private int replayTime;

    private Long workTypeId;     //근무 타입 아이디

}