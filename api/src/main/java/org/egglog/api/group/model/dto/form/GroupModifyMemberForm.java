package org.egglog.api.group.model.dto.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GroupModifyMemberForm {
    //정보를 변경할 group id
    Long groupId;
    //정보를 변경할 사용자 id
    Long groupUserId;
    //변경할 역할(변경 목표)
    Boolean groupAdmin;
}
