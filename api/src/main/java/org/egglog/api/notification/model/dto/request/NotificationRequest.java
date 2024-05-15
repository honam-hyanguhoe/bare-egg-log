package org.egglog.api.notification.model.dto.request;

import lombok.*;
import org.egglog.api.notification.model.entity.enums.TopicEnum;

import java.time.LocalDateTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.model.dto.request
 * fileName      : NotificationRequest
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-14|김형민|최초 생성|
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationRequest {
    private Long notificationId;
    private Boolean status;
}
