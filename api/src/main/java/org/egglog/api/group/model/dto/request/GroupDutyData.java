package org.egglog.api.group.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

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
    String date;
    @NotNull(message = "dutyList는 필수입니다.")
    List<String> dutyList;
}
