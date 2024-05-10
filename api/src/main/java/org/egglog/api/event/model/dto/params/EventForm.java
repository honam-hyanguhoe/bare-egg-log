package org.egglog.api.event.model.dto.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class EventForm {
    @NotBlank(message = "eventTitle 은 필수 입니다.")
    private String eventTitle;//필수

    @NotBlank(message = "eventTitle 은 필수 입니다.")
    private String eventContent;//필수

    @NotNull(message = "startDate는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;//필수

    @NotNull(message = "endDate는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;//필수

    @NotNull(message = "calendarGroupId는 필수 값 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long calendarGroupId;//필수
}
