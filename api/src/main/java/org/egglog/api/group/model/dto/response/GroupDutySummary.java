package org.egglog.api.group.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.response
 * fileName      : GroupDutySummary
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김다희|최초 생성|
 */
@Data
public class GroupDutySummary {
    public List<GroupMemberSimpleDto> day=new ArrayList<>();
    public List<GroupMemberSimpleDto> eve=new ArrayList<>();
    public List<GroupMemberSimpleDto> night=new ArrayList<>();
    public List<GroupMemberSimpleDto> off=new ArrayList<>();
    public List<GroupMemberSimpleDto> etc=new ArrayList<>();
}
