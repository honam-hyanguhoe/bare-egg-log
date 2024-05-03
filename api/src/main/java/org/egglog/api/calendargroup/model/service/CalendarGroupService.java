package org.egglog.api.calendargroup.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendar.model.service.CalendarService;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.dto.request.CreateCalGroupRequest;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupResponse;
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
    private final CalendarService calendarService;

    /**
     * 캘린더 그룹 등록<br>
     * 만약 url이 있다면, 일정등록까지 진행한다.
     * @author 김형민
     */
    public CalendarGroupResponse registerCalendarGroup(CreateCalGroupRequest request, User loginUser) {
        CalendarGroup calendarGroup = calendarGroupRepository.save(request.toEntity(loginUser));
        if (request.isInUrl()) calendarService.readCalendarFile(request.getUrl(), calendarGroup, loginUser);
        return calendarGroup.toResponse();
    }

    /**
     * 캘린더 그룹 삭제<br>
     * 속한 일정들도 함께 삭제
     *
     * @param calendarGroupId
     * @param loginUser
     */
    public void deleteCalendarGroup(Long calendarGroupId, User loginUser) {
        CalendarGroup calendarGroup = calendarGroupRepository.findById(calendarGroupId).orElseThrow(
                () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
        );
        calendarGroupRepository.delete(calendarGroup);
    }


}
