package org.egglog.api.calendargroup.model.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarGroupResponse {
    private Long calendarGroupId;
    private String url;
    private String alias;
}
