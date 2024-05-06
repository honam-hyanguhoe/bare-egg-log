package org.egglog.api.calendargroup.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.calendar.model.service.CalendarService;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.dto.params.CalendarGroupListRequest;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupEventListResponse;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupEventResponse;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (loginUser.getWorkGroupId()==calendarGroupId) throw new CalendarGroupException(CalendarGroupErrorCode.NOT_CONTROL_WORKGROUP);
        CalendarGroup calendarGroup = calendarGroupRepository.findCalendarGroupWithUserById(calendarGroupId)
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (calendarGroup.getUser().equals(loginUser)) throw new UserException(UserErrorCode.ACCESS_DENIED);
        calendarGroupRepository.delete(calendarGroup);
    }

    public List<CalendarGroupEventListResponse> getEventListByCalenderIds(CalendarGroupListRequest calendarGroupListRequest, User user) {
        List<Long> calendarGroupIds = calendarGroupListRequest.getCalendarGroupList();      //캘린더그룹 아이디

        List<CalendarGroupEventListResponse> calendarGroupEventListResponseList = new ArrayList<>();
        List<CalendarGroupEventResponse> calendarGroupEventResponseList = new ArrayList<>();

        for (Long calendarGroupId : calendarGroupIds) {
            CalendarGroup calendarGroup = calendarGroupRepository.findById(calendarGroupId).orElseThrow(
                    () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
            );
            //로그인한 사용자가 캘린더 그룹에 속한 사용자가 아닐 경우
            if (!calendarGroup.getUser().getId().equals(user.getId())) {
                throw new CalendarGroupException(CalendarGroupErrorCode.NOT_SAME_CALENDAR_GROUP_USER);
            }
            //캘린더그룹에 속한 일정들
            Optional<List<Event>> eventsByCalendarGroupId = eventRepository.findEventsByCalendarGroupId(calendarGroupId);

            if (eventsByCalendarGroupId.isPresent()) {
                for (Event event : eventsByCalendarGroupId.get()) {
                    CalendarGroupEventResponse calendarGroupEventResponse = CalendarGroupEventResponse.builder()
                            .eventId(event.getId())
                            .eventTitle(event.getEventTitle())
                            .eventContent(event.getEventContent())
                            .startDate(event.getStartDate())
                            .endDate(event.getEndDate())
                            .build();

                    calendarGroupEventResponseList.add(calendarGroupEventResponse);
                }

                CalendarGroupEventListResponse calendarGroupEventListResponse1 = CalendarGroupEventListResponse.builder()
                        .calendarGroupId(calendarGroupId)
                        .calendarGroupEventResponseList(calendarGroupEventResponseList)
                        .build();

                calendarGroupEventListResponseList.add(calendarGroupEventListResponse1);
            }
        }
        return calendarGroupEventListResponseList;
    }


}
