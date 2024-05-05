package org.egglog.api.work.model.dto.response.upcoming;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpComingCountWorkResponse {
    private String name; //근무이름
    private Long value; //해당 남은 근무의 개수
    private String color; //해당 근무의 색
}
