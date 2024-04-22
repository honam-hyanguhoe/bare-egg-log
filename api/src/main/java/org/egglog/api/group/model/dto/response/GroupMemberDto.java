package org.egglog.api.group.model.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class GroupMemberDto {
    private Long id;
    private Long userId;
    private Long groupId;
    private String userName;
    private String profileImgUrl;
    private Boolean isAdmin;
}
