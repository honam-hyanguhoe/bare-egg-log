package org.egglog.api.group.model.dto.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.egglog.api.group.model.entity.Group;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GroupRegistForm {
    String groupName;
    String groupImg;
    public Group toEntity(){
        return Group.builder()
                .groupId(null)
                .groupImg(groupImg)
                .groupName(groupName)
                .build();
    }
}
