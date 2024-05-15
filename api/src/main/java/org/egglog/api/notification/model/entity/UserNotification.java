package org.egglog.api.notification.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.notification.model.dto.response.NotificationResponse;
import org.egglog.api.notification.model.entity.enums.TopicEnum;
import org.egglog.api.user.model.entity.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.model.entity
 * fileName      : UserNotification
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-14|김형민|최초 생성|
 */
@Entity
@Getter
@Setter
@Table(name = "Notification")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    @EqualsAndHashCode.Include
    private Long notificationId;

    @Column
    @Enumerated(EnumType.STRING)
    private TopicEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Boolean status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public UserNotification updateState(Boolean status){
        this.status = status;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public NotificationResponse toResponse(){
        return NotificationResponse.builder()
                .notificationId(this.notificationId)
                .status(this.status)
                .type(this.type)
                .updatedAt(this.updatedAt)
                .build();
    }
}
