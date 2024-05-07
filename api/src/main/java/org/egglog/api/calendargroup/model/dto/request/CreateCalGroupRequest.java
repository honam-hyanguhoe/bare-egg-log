package org.egglog.api.calendargroup.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.user.model.entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CreateCalGroupRequest {

    private String alias;
    private String url;


    public boolean isInUrl(){
        return this.url != null;
    }
    public CalendarGroup toEntity(User loginUser){
        return CalendarGroup.builder()
                .alias(this.alias)
                .url(this.url!=null? this.url : null )
                .user(loginUser)
                .build();
    }
}
