package org.egglog.api.group.model.dto.request;

import lombok.Data;

import java.util.Map;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.request
 * fileName      : DutyFormat
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-17|김다희|최초 생성|
 */
@Data
public class DutyFormat {
    private String employeeId;
    private String name;
    private Map<String, String> work;
}
