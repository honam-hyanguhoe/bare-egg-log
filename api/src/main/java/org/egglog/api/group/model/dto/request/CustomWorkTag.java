package org.egglog.api.group.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public CustomWorkTag(){
        this.day="";
        this.eve="";
        this.off="";
        this.night="";
    }

    public Map<String, WorkTag> toMap(){
        Map<String, WorkTag> workTagMap = new HashMap<>();
        workTagMap.put(this.day, WorkTag.DAY);
        workTagMap.put(this.eve, WorkTag.EVE);
        workTagMap.put(this.night, WorkTag.NIGHT);
        workTagMap.put(this.off, WorkTag.OFF);
        return workTagMap;
    }
}
