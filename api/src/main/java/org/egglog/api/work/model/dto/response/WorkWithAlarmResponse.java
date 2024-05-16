package org.egglog.api.work.model.dto.response;

import lombok.*;
import org.egglog.api.alarm.model.dto.response.AlarmOutputSpec;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;

import java.time.LocalDate;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.response
 * fileName      : WorkWithAlarmResponse
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-16|김형민|최초 생성|
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkWithAlarmResponse {
    private Long workId;
    private LocalDate workDate;
    private WorkTypeResponse workType;
    private AlarmOutputSpec alarm;
}
