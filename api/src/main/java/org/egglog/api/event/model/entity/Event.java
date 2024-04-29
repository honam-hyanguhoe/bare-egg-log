package org.egglog.api.event.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.user.model.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_calendar_group_id", columnList = "calendar_group_id"))
@Builder(toBuilder = true)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "event_content")
    private String eventContent;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_group_id")
    private CalendarGroup calendarGroup;
}
