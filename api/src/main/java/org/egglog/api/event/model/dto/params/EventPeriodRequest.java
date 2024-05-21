package org.egglog.api.event.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.event.model.dto.params
 * fileName      : EventPeriodRequest
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-03|김도휘|최초 생성|
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class EventPeriodRequest {

    @NotNull(message = "startDate는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate; //필수

    @NotNull(message = "endDate는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate; //필수

    @NotNull(message = "userId는 필수 값 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long userId; //필수

    @NotNull(message = "calendarGroupId는 필수 값 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long calendarGroupId; //필수
}
