package org.egglog.api.work.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.request
 * fileName      : FindMonthlyGroupUserWorkListRequest
 * description    : 같은 그룹에 속해있는 유저의 근무일정을 조회 요청 객체
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindGroupUserWorkListRequest {
    @NotNull(message = "userGroupId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long userGroupId;

    @NotNull(message = "targetUserId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long targetUserId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
