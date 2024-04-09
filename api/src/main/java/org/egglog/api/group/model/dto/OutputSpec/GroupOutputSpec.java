package org.egglog.api.group.model.dto.OutputSpec;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GroupOutputSpec {
    Long groupId;
    String groupName;
    String groupImg;
    Boolean groupAdmin;
}
