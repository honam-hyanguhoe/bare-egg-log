package org.egglog.api.calendar.model.service;

import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;
import org.checkerframework.checker.units.qual.C;
import org.egglog.api.calendar.model.dto.params.CalendarMonthRequest;
import org.egglog.api.calendar.model.dto.response.CalendarListResponse;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupResponse;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.event.model.dto.response.EventListOutputSpec;
import org.egglog.api.event.model.entity.Event;
import org.egglog.api.event.model.service.EventService;
import org.egglog.api.event.repository.jpa.EventCustomQueryImpl;
import org.egglog.api.event.repository.jpa.EventRepository;
import org.egglog.api.work.model.dto.response.WorkListResponse;
import org.egglog.api.work.model.dto.response.WorkResponse;
import org.egglog.api.work.model.entity.Work;
import org.egglog.api.work.repository.jpa.WorkQueryRepository;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;
import org.egglog.api.worktype.model.entity.WorkType;
import org.intellij.lang.annotations.JdkConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final EventRepository eventRepository;

    private final WorkQueryRepository workQueryRepository;

    private final CalendarGroupRepository calendarGroupRepository;


    public void createCalendar() {
        Calendar calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.add(ImmutableVersion.VERSION_2_0);
        calendar.add(ImmutableCalScale.GREGORIAN);

    }

    /**
     * 한달 조회
     * 개인 일정 + 근무
     *
     * @param calendarMonthRequest
     * @return
     */
    public CalendarListResponse getCalendarListByMonth(CalendarMonthRequest calendarMonthRequest) {
        LocalDateTime startDate = calendarMonthRequest.getStartDate();
        LocalDateTime endDate = calendarMonthRequest.getEndDate();
        Long calendarGroupId = calendarMonthRequest.getCalendarGroupId();
        Long userId = calendarMonthRequest.getUserId();

        CalendarGroup calendarGroup = calendarGroupRepository.findById(calendarGroupId).orElseThrow(
                () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
        );

        List<Work> workList = workQueryRepository.findWorkListAllByTime(calendarMonthRequest.getCalendarGroupId(), startDate.toLocalDate(), endDate.toLocalDate());
        Optional<List<Event>> eventsByMonthAndUserId = eventRepository.findEventsByMonthAndUserId(startDate, endDate, userId, calendarGroupId);

        List<WorkResponse> workResponseList = new ArrayList<>();
//        private List<WorkResponse> workList;
//        private CalendarGroupResponse calendarGroup;
        List<EventListOutputSpec> eventListOutputSpecList = new ArrayList<>();

        for (Work work : workList) {
            WorkType workType = work.getWorkType();
            WorkTypeResponse workTypeResponse = WorkTypeResponse.builder()
                    .workTypeId(workType.getId())
                    .workTypeImgUrl(workType.getWorkTypeImgUrl())
                    .color(workType.getColor())
                    .title(workType.getTitle())
                    .startTime(workType.getStartTime())
                    .endTime(workType.getEndTime())
                    .build();

            WorkResponse workResponse = WorkResponse.builder()
                    .workId(work.getId())
                    .workDate(work.getWorkDate())
                    .workType(workTypeResponse)
                    .build();

            workResponseList.add(workResponse);
        }

        CalendarGroupResponse calendarGroupResponse = CalendarGroupResponse.builder()
                .calendarGroupId(calendarGroupId)
                .url(calendarGroup.getUrl())
                .alias(calendarGroup.getAlias())
                .build();

        WorkListResponse workListResponse = WorkListResponse.builder()
                .workList(workResponseList)
                .calendarGroup(calendarGroupResponse)
                .build();

        if (eventsByMonthAndUserId.isPresent()) {
            for (Event event : eventsByMonthAndUserId.get()) {
                EventListOutputSpec eventListOutputSpec = EventListOutputSpec.builder()
                        .eventId(event.getId())
                        .eventTitle(event.getEventTitle())
                        .eventContent(event.getEventContent())
                        .startDate(event.getStartDate())
                        .endDate(event.getEndDate())
                        .calendarGroupId(event.getCalendarGroup().getId())
                        .build();

                eventListOutputSpecList.add(eventListOutputSpec);
            }
        }
        CalendarListResponse calendarListResponse = CalendarListResponse.builder()
                .workList(workListResponse)
                .eventList(eventListOutputSpecList)
                .build();
        
        return calendarListResponse;
    }


}
