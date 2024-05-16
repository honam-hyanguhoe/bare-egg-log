package org.egglog.api.group.model.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.request
 * fileName      : GroupDutyDto
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-16|김다희|최초 생성|
 */
@Data
@SuperBuilder
public class GroupDutyDto {
    private String day;
    private String userName;
    private GroupDutyDataDto dutyData;
}
