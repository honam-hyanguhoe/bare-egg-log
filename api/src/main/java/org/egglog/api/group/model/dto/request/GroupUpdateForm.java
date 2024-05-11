package org.egglog.api.group.model.dto.request;

import lombok.Data;

@Data
public class GroupUpdateForm {
    private String newName = null;
    private String newPassword =null;
}
