package org.egglog.api.calendar.model.dto.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.calendar.model.dto.params
 * fileName      : CalendarMonthRequest
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김도휘|최초 생성|
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CalendarMonthRequest {

    @NotNull(message = "startDate는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) //필수
    private LocalDateTime startDate;

    @NotNull(message = "endDate는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)//필수
    private LocalDateTime endDate;

    @NotNull(message = "calendarGroupId는 필수 값 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long calendarGroupId;//필수
}
