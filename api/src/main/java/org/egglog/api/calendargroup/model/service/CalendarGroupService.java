package org.egglog.api.calendargroup.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.dto.params.CalendarGroupForm;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.event.model.entity.Event;
import org.egglog.api.event.repository.jpa.EventRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarGroupService {

    private final CalendarGroupRepository calendarGroupRepository;

    private final UserJpaRepository userJpaRepository;

    private final EventRepository eventRepository;


    public void registerCalendarGroup(CalendarGroupForm calendarGroupForm, User user) {
        CalendarGroup calendarGroup = CalendarGroup.builder()
                .url(calendarGroupForm.getUrl())
                .alias(calendarGroupForm.getAlias())
                .user(user)
                .build();

        calendarGroupRepository.save(calendarGroup);

    }

    /**
     * 캘린더 그룹 삭제<br>
     * 속한 일정들도 함께 삭제
     *
     * @param calendarGroupId
     * @param userId
     */
    public void deleteCalendarGroup(Long calendarGroupId, Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        CalendarGroup calendarGroup = calendarGroupRepository.findById(calendarGroupId).orElseThrow(
                () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
        );

        if (calendarGroup.getUser().getId().equals(user.getId())) {
            calendarGroupRepository.delete(calendarGroup);
            Optional<List<Event>> eventList = eventRepository.findEventsByCalendarGroupId(calendarGroupId);

            //캘린더 그룹에 속한 일정들 삭제
            eventList.ifPresent(eventRepository::deleteAll);
        }
    }


}
