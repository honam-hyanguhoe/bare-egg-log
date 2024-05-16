package org.egglog.api.group.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.request
 * fileName      : GroupDutySaveFormat
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-16|김다희|최초 생성|
 */
@Data
@Builder
public class GroupDutySaveFormat {
    private List<String> dutyList;
    //없는 경우 기존 값으로 저장됨
    private CustomWorkTag customWorkTag;
    private String userName;
    private String day;
}
