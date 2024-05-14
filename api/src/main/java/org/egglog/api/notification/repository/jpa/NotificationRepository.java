package org.egglog.api.notification.repository.jpa;

import org.egglog.api.notification.model.entity.UserNotification;

import org.egglog.api.notification.model.entity.enums.TopicEnum;
import org.egglog.api.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<UserNotification, Long>, NotificationQueryRepository {

    Optional<UserNotification> findByTypeAndUser(TopicEnum type, User user);
}
