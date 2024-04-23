package org.egglog.api.group.model.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

public class GroupDto {
    private Long id;
    private Integer groupImage;
    private String groupName;
    private GroupMemberDto admin;
    private List<GroupMemberDto> groupMembers;

}
