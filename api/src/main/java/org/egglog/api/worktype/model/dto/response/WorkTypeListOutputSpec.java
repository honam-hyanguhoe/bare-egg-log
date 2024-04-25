package org.egglog.api.worktype.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkTypeListOutputSpec {

    private Long id;

    private String name;

    private String color;

    private String imgUrl;

    private LocalTime startTime;       //시작 시간

    private LocalTime endTime;     //종료 시간
}
