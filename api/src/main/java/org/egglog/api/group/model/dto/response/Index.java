package org.egglog.api.group.model.dto.response;

import lombok.Data;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.model.dto.response
 * fileName      : Index
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-17|김다희|최초 생성|
 */
@Data
public class Index{
    public String groupId;
    public String date;
    public String index;

    public Index(String groupId, String date, String index){
        this.groupId=groupId;
        this.date=date;
        this.index=index;
    }
}
