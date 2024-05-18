package org.egglog.api.work.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.alarm.model.entity.Alarm;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.model.dto.response.WorkResponse;
import org.egglog.api.work.model.dto.response.WorkWithAlarmResponse;
import org.egglog.api.worktype.model.entity.WorkType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.entity
 * fileName      : Work
 * description    : 근무 일정 엔티티
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(indexes = {@Index(name = "idx_user_id", columnList = "user_id"),@Index(name = "idx_work_uuid", columnList = "work_uuid")})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "work_date")
    private LocalDate workDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_type_id")
    private WorkType workType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_group_id")
    private CalendarGroup calendarGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "work_uuid")
    private String uuid;

    public WorkResponse toResponse(){
        return WorkResponse.builder()
                .workId(this.id)
                .workDate(this.workDate)
                .workType(this.workType.toResponse())
                .build();
    }

    public Work updateWorkType(WorkType updateWorkType){
        this.workType = updateWorkType;
        return this;
    }

    public WorkWithAlarmResponse toResponse(Map<WorkType, Alarm> alarmMap){
        return WorkWithAlarmResponse.builder()
                .work(this.toResponse())
                .alarm(alarmMap.get(this.workType).toOutputSpec())
                .build();
    }
}
