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
 * fileName      : SyncExcelWorkRequest
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-17|김형민|최초 생성|
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SyncExcelWorkRequest {
    @NotNull(message = "그룹 ID는 필수 입니다.")
    @Positive(message = "정확한 ID값을 입력 해주세요.")
    private Long groupId;
    @NotNull(message = "yyyy-mm-dd 형식의 타겟 월을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate targetMonth;
    @NotNull(message = "excel index는 필수 입니다.")
    @Positive(message = "정확한 index 값을 입력 해주세요.")
    private Long index;
}
