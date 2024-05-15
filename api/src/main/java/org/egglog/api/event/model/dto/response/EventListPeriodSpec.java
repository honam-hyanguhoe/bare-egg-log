package org.egglog.api.event.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class EventListPeriodSpec {

    private LocalDate date;

    private List<EventListOutputSpec> eventList;    //개인 일정 리스트

    public EventListPeriodSpec(LocalDate date) {
        this.date = date;
        this.eventList = new ArrayList<>();
    }

    public void addEvent(EventListOutputSpec event) {
        if (this.eventList == null) {
            this.eventList = new ArrayList<>();
        }
        this.eventList.add(event);
    }
}
