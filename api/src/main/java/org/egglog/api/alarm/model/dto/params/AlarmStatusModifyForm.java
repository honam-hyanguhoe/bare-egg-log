package org.egglog.api.alarm.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmStatusModifyForm {
    @NotNull(message = "alarmId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long alarmId;//필수
    @NotNull(message = "isAlarmOn 는 필수 입니다. 켜짐 T, 꺼짐 F")
    private Boolean isAlarmOn;//필수
}
