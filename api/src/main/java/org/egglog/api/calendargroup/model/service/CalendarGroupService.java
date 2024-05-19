package org.egglog.api.calendargroup.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarGroupService {

    private final CalendarGroupRepository calendarGroupRepository;
    private final EventRepository eventRepository;
    private final CalendarService calendarService;

    /**
     * 캘린더 그룹 등록<br>
     * 만약 url이 있다면, 일정등록까지 진행한다.
     * @author 김형민
     */
    public CalendarGroupResponse registerCalendarGroup(CreateCalGroupRequest request, User loginUser) {
        log.debug(" ==== ==== ==== [ 캘린더 그룹 생성 서비스 실행 ] ==== ==== ====");
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
     * @author 김형민
     */
    @Transactional
    public void deleteCalendarGroup(Long calendarGroupId, User loginUser) {
        log.debug(" ==== ==== ==== [ 캘린더 그룹 삭제 서비스 실행 ] ==== ==== ====");
        if (loginUser.getWorkGroupId().equals(calendarGroupId)) throw new CalendarGroupException(CalendarGroupErrorCode.NOT_CONTROL_WORKGROUP);
        CalendarGroup calendarGroup = calendarGroupRepository.findCalendarGroupWithUserById(calendarGroupId)
                .orElseThrow(() -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP));
        if (!calendarGroup.getUser().equals(loginUser)) throw new UserException(UserErrorCode.ACCESS_DENIED);
        long deletedCount = eventRepository.deleteAllByCalendarGroupId(calendarGroupId);
        log.debug(" ==== ==== ==== [ 캘린더 ID = {} 에 대한 값 삭제 관련 일정 = {} 개 삭제 완료 ] ==== ==== ==== ",calendarGroupId, deletedCount);
        calendarGroupRepository.delete(calendarGroup);
    }

    public List<CalendarGroupResponse> findCalendarGroupList(User loginUser){
        log.debug(" ==== ==== ==== [ 캘린더 그룹 리스트 조회 서비스 실행 ] ==== ==== ====");
        return calendarGroupRepository.findListByUserId(loginUser.getId())
                .stream()
                .map(CalendarGroup::toResponse)
                .collect(Collectors.toList());
    }

    public List<CalendarGroupEventListResponse> getEventListByCalenderIds(CalendarGroupListRequest calendarGroupListRequest, User user) {
        log.debug(" ==== ==== ==== [ 캘린더 그룹 ID 리스트로 개인 일정 리스트 조회 서비스 실행 ] ==== ==== ====");
        List<Long> calendarGroupIds = calendarGroupListRequest.getCalendarGroupList();      //캘린더그룹 아이디

        List<CalendarGroupEventListResponse> calendarGroupEventListResponseList = new ArrayList<>();
        Map<Long, CalendarGroup> userCalendarGroupMap = calendarGroupRepository.findListByUserId(user.getId())
                .stream()
                .collect(Collectors.toMap(CalendarGroup::getId, calendarGroup -> calendarGroup));

        for (Long requestId : calendarGroupIds) {
            if (!userCalendarGroupMap.containsKey(requestId)) throw new UserException(UserErrorCode.ACCESS_DENIED);
            CalendarGroup calendarGroup = userCalendarGroupMap.get(requestId);

            //캘린더그룹에 속한 일정들
            calendarGroupEventListResponseList.add(
                    CalendarGroupEventListResponse.builder()
                            .calendarGroupId(calendarGroup.getId())
                            .calendarGroupEventResponseList(eventRepository.findEventsByCalendarGroupId(calendarGroup.getId()).get()
                                    .stream()
                                    .map(Event::toCalendarGroupEventResponse)
                                    .collect(Collectors.toList()))
                            .build()
            );
        }
        return calendarGroupEventListResponseList;
    }


    @Transactional
    public void deleteUserCalendarGroups(User loginUser){
        List<CalendarGroup> listByUserId = calendarGroupRepository.findListByUserId(loginUser.getId());
        for (CalendarGroup calendarGroup : listByUserId) {
            long deletedCount = eventRepository.deleteAllByCalendarGroupId(calendarGroup.getId());
            log.debug(" ==== ==== ==== [ 캘린더 = {} 에 대한 일정 값 삭제 관련 일정 = {} 개 삭제 완료 ] ==== ==== ==== ",calendarGroup.getAlias(), deletedCount);
            calendarGroupRepository.delete(calendarGroup);
        }
    }
}
