package org.egglog.api.notification.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.notification.model.entity.UserNotification;
import org.egglog.api.notification.model.entity.QUserNotification;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.egglog.api.notification.model.entity.QUserNotification.userNotification;
/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.repository.jpa
 * fileName      : NotificationQueryRepositoryImpl
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-14|김형민|최초 생성|
 */
@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Map<Long, UserNotification> findMapByUser(User user) {
        return jpaQueryFactory.selectFrom(userNotification)
                .where(userNotification.user.eq(user))
                .fetch()
                .stream()
                .collect(Collectors.toMap(UserNotification::getNotificationId, notification -> notification));
    }

    @Override
    public List<UserNotification> findListByUser(User user){
        return jpaQueryFactory.selectFrom(userNotification)
                .where(userNotification.user.eq(user))
                .fetch();
    }
}
