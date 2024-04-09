package org.egglog.api.group.model.dto.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GroupRegistMemberForm {
    //정보가 수정될 수 있어서 dto를 modify form과 분리함
    Long groupId;
    Long groupUserId;
}
