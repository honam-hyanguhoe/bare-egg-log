package org.egglog.api.group.model.dto.OutputSpec;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleGroupUser {
    String name;
    String empNo;
}
