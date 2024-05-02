package org.egglog.api.work.model.dto.response.completed;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletedWorkCountWeekResponse {
    private Integer week;
    private Map<String, Integer> data;
}
