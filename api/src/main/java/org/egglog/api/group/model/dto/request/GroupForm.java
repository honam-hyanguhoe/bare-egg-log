package org.egglog.api.group.model.dto.request;

//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import jakarta.persistence.Column;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class GroupForm {
    private Integer groupImage;
    private String groupPassword;
    private String groupName;
}
