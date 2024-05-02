package org.egglog.api.work.model.dto.response.completed;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletedWorkCountResponse {
    private String month; //yyyy-mm
    private List<CompletedWorkCountWeekResponse> weeks;
}
