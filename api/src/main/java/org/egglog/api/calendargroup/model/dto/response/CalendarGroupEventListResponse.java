package org.egglog.api.calendargroup.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.calendargroup.model.dto.params
 * fileName      : CalendarGroupEventListResponse
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
public class CalendarGroupEventListResponse {

    private Long calendarGroupId;

    private List<CalendarGroupEventResponse> calendarGroupEventResponseList;
}
