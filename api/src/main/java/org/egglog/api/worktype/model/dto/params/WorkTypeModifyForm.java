package org.egglog.api.worktype.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkTypeModifyForm {

    private Long id;

    private String title;

    private String startTime;       //시작 시간

    private String endTime;     //종료 시간

    private String color;

    private Long userId;

    // 사진 변경 X
}
