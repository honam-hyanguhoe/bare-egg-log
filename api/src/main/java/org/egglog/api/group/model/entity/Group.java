package org.egglog.api.group.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_image", nullable = false)
    private Integer groupImage;

    @Column(name = "group_name",length = 100, nullable = false)
    private String groupName;

    @Transient
    private GroupMember bossMember;

    @Transient
    private List<GroupMember> memberList;


}
