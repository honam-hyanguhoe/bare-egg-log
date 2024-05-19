package org.egglog.api.group.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.egglog.api.group.model.dto.request.CustomWorkTag;

import java.util.Map;

@Data
@AllArgsConstructor
public class UserDutyDataDto {
    private Map<String, String> dutyData;
    private CustomWorkTag customWorkTag;
}
