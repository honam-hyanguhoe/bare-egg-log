package org.egglog.api.group.model.dto.request;

import lombok.Data;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.request
 * fileName      : CustomWorkTag
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-16|김다희|최초 생성|
 */
@Data
public class CustomWorkTag {
    private String day;
    private String eve;
    private String night;
    private String off;
}
