package org.egglog.api.group.model.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.egglog.api.group.model.dto.request.CustomWorkTag;

import java.util.List;
import java.util.Map;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.response
 * fileName      : GroupDutyData
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-16|김다희|최초 생성|
 */
@Data
@SuperBuilder
public class GroupDutyDataDto {
    private Map<String, List<String>> dutyList;
    //없는 경우 기존 값으로 저장됨
    private CustomWorkTag customWorkTag;
    private String groupId;
    private String date;
    private String index;
}
