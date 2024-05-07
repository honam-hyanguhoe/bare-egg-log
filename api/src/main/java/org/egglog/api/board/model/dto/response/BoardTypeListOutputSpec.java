package org.egglog.api.board.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.group.model.dto.response.GroupSimpleDto;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.board.model.dto.response
 * fileName      : BoardTypeListOutputSpec
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-07|김도휘|최초 생성|
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BoardTypeListOutputSpec {

    private Long hospitalId;

    private String hospitalName;

    private List<GroupPreviewDto> groupList;
}
