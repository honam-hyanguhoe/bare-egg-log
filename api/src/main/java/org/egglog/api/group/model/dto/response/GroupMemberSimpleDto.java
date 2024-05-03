package org.egglog.api.group.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.response
 * fileName      : GroupMemberSimpleDto
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김다희|최초 생성|
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GroupMemberSimpleDto {
    private Long userId;
    private String userName;
    private String profileImgUrl;
}
