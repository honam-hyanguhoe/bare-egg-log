package org.egglog.api.notification.repository.jpa;

import org.egglog.api.notification.model.entity.UserNotification;
import org.egglog.api.user.model.entity.User;

import java.util.List;
import java.util.Map;

public interface NotificationQueryRepository {
    Map<Long, UserNotification> findMapByUser(User user);

    List<UserNotification> findListByUser(User user);
}
