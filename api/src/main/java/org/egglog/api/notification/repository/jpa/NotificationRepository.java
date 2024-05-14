package org.egglog.api.notification.repository.jpa;

import org.egglog.api.notification.model.entity.UserNotification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<UserNotification, Long>, NotificationQueryRepository {
}
