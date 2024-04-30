package org.egglog.api.calendar.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.egglog.api.event.model.dto.response.EventListOutputSpec;
import org.egglog.api.work.model.dto.response.WorkListResponse;

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

    List<EventListOutputSpec> eventList;    //개인 일정 리스트

    WorkListResponse workList;    //근무 일정 리스트

}
