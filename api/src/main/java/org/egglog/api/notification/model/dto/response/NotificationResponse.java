package org.egglog.api.notification.model.dto.response;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.notification.model.entity.enums.TopicEnum;
import org.egglog.api.user.model.entity.User;

import java.time.LocalDateTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.model.dto.response
 * fileName      : NotificationResponse
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-14|김형민|최초 생성|
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long notificationId;

    private TopicEnum type;

    private boolean status;

    private LocalDateTime updatedAt;
}
