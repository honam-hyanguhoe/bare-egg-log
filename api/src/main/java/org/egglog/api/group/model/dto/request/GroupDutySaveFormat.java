package org.egglog.api.group.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egglog.api.group.model.dto.response.Index;

import java.util.List;
import java.util.Map;

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
@NoArgsConstructor
@AllArgsConstructor
public class GroupDutySaveFormat {
    private Map<String,DutyFormat> dutyList;
    //없는 경우 기존 값으로 저장됨
    private CustomWorkTag customWorkTag;
    private String userName;
    private String day;
}
