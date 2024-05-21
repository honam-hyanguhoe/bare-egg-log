package org.egglog.api.group.model.dto.request;

import lombok.Data;

@Data
public class GroupUpdateForm {
    private String newName;
    private String newPassword;
}
