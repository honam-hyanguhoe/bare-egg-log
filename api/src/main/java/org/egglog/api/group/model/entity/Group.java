package org.egglog.api.group.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.egglog.api.group.model.dto.response.GroupSimpleDto;

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

    @Column(name = "group_password",nullable = false)
    private String password;

    @Column(name = "group_admin", nullable = false)
    private String admin;

    public GroupSimpleDto toSimpleDto(){
        GroupSimpleDto simpleDto = new GroupSimpleDto();
        simpleDto.setGroupId(this.id);
        simpleDto.setGroupName(this.groupName);
        return simpleDto;
    }
}
