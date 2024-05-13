package org.egglog.api.calendar.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.egglog.api.event.model.dto.response.EventListOutputSpec;
import org.egglog.api.work.model.dto.response.WorkListResponse;
import org.egglog.api.work.model.dto.response.WorkResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.calendar.model.dto.response
 * fileName      : CalendarListResponse
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
public class CalendarListResponse {

    private LocalDate date;

    private List<EventListOutputSpec> eventList;    //개인 일정 리스트

    private WorkListResponse workList;    //근무 일정 리스트

    private Long calendarGroupId;

    public CalendarListResponse(LocalDate date) {
        this.date = date;
        this.eventList = new ArrayList<>();
        this.workList = new WorkListResponse();  // 초기화
    }

    public void addEvent(EventListOutputSpec event) {
        if (this.eventList == null) {
            this.eventList = new ArrayList<>();
        }
        this.eventList.add(event);
    }

    public void addWorkToWorkList(WorkResponse work) {
        if (this.workList == null) {
            this.workList = new WorkListResponse();  // 만약 workList가 null이라면 초기화
        }
        this.workList.addWork(work);  // WorkListResponse 클래스의 addWork 메서드 호출
    }
}
