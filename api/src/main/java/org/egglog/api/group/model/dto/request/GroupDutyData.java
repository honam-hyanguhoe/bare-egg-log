package org.egglog.api.group.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.egglog.api.worktype.model.entity.WorkTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.request
 * fileName      : GroupDutyData
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-13|김다희|최초 생성|
 */
@Data
public class GroupDutyData {
    @NotBlank(message = "date 는 필수입니다.")
    private String date;
    @NotNull(message = "dutyList는 필수입니다.")
    private List<List<String>> dutyList;
    //없는 경우 기존 값으로 저장됨
    private CustomWorkTag customWorkTag;
    private String userName;
    private String day;

    public Map<String, List<String>> toMap(){
        Map<String, List<String>> dutyMap = new HashMap<>();
        for(List<String> dutyData : this.dutyList){
            String key = dutyData.get(0);
            dutyData.remove(0);
            dutyData.remove(0);
            dutyMap.put(key,dutyData);
        }
        return dutyMap;
    }
}
