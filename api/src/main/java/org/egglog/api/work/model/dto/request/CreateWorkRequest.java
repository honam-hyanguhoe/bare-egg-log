package org.egglog.api.work.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.model.entity.Work;
import org.egglog.api.worktype.model.entity.WorkType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;


/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.request
 * fileName      : WorkCreateRequest
 * description    : 근무일정 추가 요청 객체
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 * |2024-05-06|김형민|검증 추가|
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateWorkRequest {
    @NotNull(message = "workDate 는 필수 입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate workDate;

    @NotNull(message = "근무 타입은 필수 입니다..")
    @Positive(message = "정확한 ID값을 입력 해주세요.")
    private Long workTypeId;

    public Work toEntity(User user, Map<Long, WorkType> userWorkTypeMap, CalendarGroup calendarGroup){
        UidGenerator ug = new RandomUidGenerator();
        return Work.builder()
                .user(user)
                .workType(userWorkTypeMap.get(this.workTypeId))
                .calendarGroup(calendarGroup)
                .workDate(this.workDate)
                .uuid(ug.generateUid().getValue())
                .build();
    }
}
