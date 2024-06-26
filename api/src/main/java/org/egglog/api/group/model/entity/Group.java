package org.egglog.api.group.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.group.model.dto.response.GroupSimpleDto;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "`Groups`")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_image", nullable = false)
    private Integer groupImage;

    @Column(name = "group_name",length = 100, nullable = false)
    private String groupName;

    @Column(name = "group_password",nullable = false)
    private String password;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

    public GroupSimpleDto toSimpleDto(){
        GroupSimpleDto simpleDto = new GroupSimpleDto();
        simpleDto.setGroupId(this.id);
        simpleDto.setGroupName(this.groupName);
        return simpleDto;
    }

    public Group update(String groupName, String password){
        this.groupName = groupName==null||groupName.isEmpty() ? this.groupName : groupName;
        this.password = password==null||password.isEmpty() ? this.password : password;
        return this;
    }
}
