package org.egglog.api.group.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.user.model.entity.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groups_id")
    private Group group;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    public GroupMemberDto toDto(){
        return GroupMemberDto.builder()
                .groupId(group.getId())
                .userName(user.getName())
                .profileImgUrl(user.getProfileImgUrl())
                .isAdmin(this.isAdmin)
                .build();
    }
}
