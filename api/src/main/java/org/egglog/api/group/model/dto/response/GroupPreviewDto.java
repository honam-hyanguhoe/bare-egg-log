package org.egglog.api.group.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class GroupPreviewDto {
    private Long groupId;
    private Integer groupImage;
    private String groupName;
    private String admin;
    private Integer memberCount;

    public GroupPreviewDto(Long groupId,Integer groupImage,String groupName, String admin, Long memberCount){
        this.groupId=groupId;
        this.groupImage=groupImage;
        this.groupName=groupName;
        this.admin=admin;
        this.memberCount=memberCount.intValue();
    }
}
