package org.egglog.api.calendargroup.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupResponse;
import org.egglog.api.user.model.entity.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public class CalendarGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "calendar_group_id")
    private Long id;

    @Column(name = "calendar_group_url")
    private String url;

    @Column(name = "calendar_group_alias")
    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public CalendarGroupResponse toResponse(){
        return CalendarGroupResponse.builder()
                .calendarGroupId(this.id)
                .alias(this.alias)
                .url(this.url)
                .build();
    }
}
