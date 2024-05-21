package org.egglog.api.alarm.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmModifyForm {
    @NotNull(message = "alarmId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long alarmId;//필수

    @NotNull(message = "alarmTime 는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime alarmTime;//필수

    private Integer replayCnt;

    private Integer replayTime;

    @NotNull(message = "workTypeId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long workTypeId;//필수
}
