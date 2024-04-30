package org.egglog.api.work.model.dto.response;


import lombok.*;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupResponse;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;

import java.time.LocalDate;


/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.response
 * fileName      : WorkCreateListResponse
 * description    : 근무 일정 응답 객체
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkResponse {

    private Long workId;
    private LocalDate workDate;
    private WorkTypeResponse workType;
    private CalendarGroupResponse calendarGroup;
    private User user;

}
