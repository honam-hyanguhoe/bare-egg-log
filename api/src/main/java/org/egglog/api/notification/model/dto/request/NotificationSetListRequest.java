package org.egglog.api.notification.model.dto.request;

import lombok.*;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.model.dto.request
 * fileName      : NotificationSetListRequest
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-17|김형민|최초 생성|
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationSetListRequest {
    private List<NotificationRequest> notificationSetList;
}
