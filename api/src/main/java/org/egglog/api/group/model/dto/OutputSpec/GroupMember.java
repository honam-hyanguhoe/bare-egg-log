package org.egglog.api.group.model.dto.OutputSpec;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupMember {
    Long groupId;
    Long userId;
    Boolean groupAdmin;
}
