package org.egglog.api.event.model.service;

import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.antlr.v4.runtime.misc.FlexibleHashMap;
import org.egglog.api.calendar.model.dto.response.CalendarListResponse;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.event.model.dto.params.EventForm;
import org.egglog.api.event.model.dto.params.EventPeriodRequest;
import org.egglog.api.event.model.dto.params.EventUpdateForm;
import org.egglog.api.event.model.dto.response.EventListOutputSpec;
import org.egglog.api.event.model.dto.response.EventListPeriodSpec;
import org.egglog.api.event.model.dto.response.EventOutputSpec;
import org.egglog.api.event.exception.EventErrorCode;
import org.egglog.api.event.exception.EventException;
import org.egglog.api.event.model.entity.Event;
import org.egglog.api.event.repository.jpa.EventRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserJpaRepository userJpaRepository;
    private final CalendarGroupRepository calendarGroupRepository;

    /**
     * 개인 일정 등록
     *
     * @param eventForm
     * @param user
     */
    public void registerEvent(EventForm eventForm, User user) {
        CalendarGroup calendarGroup = calendarGroupRepository.findById(eventForm.getCalendarGroupId()).orElseThrow(
                () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
        );
        UidGenerator ug = new RandomUidGenerator();
        Event event = Event.builder()
                .eventTitle(eventForm.getEventTitle())
                .eventContent(eventForm.getEventContent())
                .startDate(eventForm.getStartDate())
                .endDate(eventForm.getEndDate())
                .user(user)     //사용자
                .calendarGroup(calendarGroup)   //캘린더 그룹
                .uuid(ug.generateUid().getValue())
                .build();

        eventRepository.save(event);

    }

    /**
     * 개인 일정 리스트 조회
     *
     * @param userId
     * @return
     */
    public List<EventListOutputSpec> getEventList(Long userId) {
        List<Event> eventList = eventRepository.findAllByUserId(userId);
        if (eventList.isEmpty()) {
            return null;
        }
        List<EventListOutputSpec> eventListOutputSpecList = new ArrayList<>();

        for (Event event : eventList) {

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
        return eventListOutputSpecList;
    }

    public EventOutputSpec getEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventErrorCode.NOT_FOUND_EVENT)
        );
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        return EventOutputSpec.builder()
                .eventId(event.getId())
                .eventTitle(event.getEventTitle())
                .eventContent(event.getEventContent())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .calendarGroupId(event.getCalendarGroup().getId())
                .build();
    }

    public List<EventListPeriodSpec> getEventListByPeriod(EventPeriodRequest eventPeriodRequest, User user) {
        Map<LocalDate, EventListPeriodSpec> dailyEvents = new HashMap<>();

        LocalDate start = eventPeriodRequest.getStartDate();
        LocalDate end = eventPeriodRequest.getEndDate();
        Long userId = eventPeriodRequest.getUserId();
        Long calendarGroupId = eventPeriodRequest.getCalendarGroupId();

        CalendarGroup calendarGroup = calendarGroupRepository.findById(calendarGroupId).orElseThrow(
                () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
        );
        List<EventListOutputSpec> eventListOutputSpecList = new ArrayList<>();

        //캘린더그룹 사용자와 로그인한 사용자가 다르다면
        if (!user.getId().equals(calendarGroup.getUser().getId())) {
            throw new CalendarGroupException(CalendarGroupErrorCode.NOT_SAME_CALENDAR_GROUP_USER);
        }
        Optional<List<Event>> eventsByMonthAndUserId = eventRepository.findEventsByMonthAndUserId(start, end, userId, calendarGroupId);

        if (eventsByMonthAndUserId.isPresent()) {
            for (Event event : eventsByMonthAndUserId.get()) {
                LocalDate eventStart = event.getStartDate().toLocalDate();
                LocalDate eventEnd = event.getEndDate().toLocalDate();

                EventListOutputSpec eventListOutputSpec = EventListOutputSpec.builder()
                        .eventId(event.getId())
                        .eventTitle(event.getEventTitle())
                        .eventContent(event.getEventContent())
                        .startDate(event.getStartDate())
                        .endDate(event.getEndDate())
                        .build();

                eventListOutputSpecList.add(eventListOutputSpec);

                eventStart.datesUntil(eventEnd.plusDays(1)).forEach(date -> {
                    EventListPeriodSpec schedule = dailyEvents.get(date);
                    if (schedule == null) {
                        schedule = new EventListPeriodSpec(date);
                        dailyEvents.put(date, schedule);
                    }
                    schedule.addEvent(eventListOutputSpec);
                });
            }
        }
        List<EventListPeriodSpec> result = new ArrayList<>();
        for (EventListPeriodSpec value : dailyEvents.values()) {
            result.add(value);
        }
        return result;
    }

    @Transactional
    public EventOutputSpec modifyEvent(Long eventId, EventUpdateForm eventUpdateForm, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventErrorCode.NOT_FOUND_EVENT)
        );
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        event.setEventTitle(eventUpdateForm.getEventTitle());
        event.setEventContent(eventUpdateForm.getEventContent());
        event.setStartDate(eventUpdateForm.getStartDate());
        event.setEndDate(eventUpdateForm.getEndDate());

        return EventOutputSpec.builder()
                .eventId(event.getId())
                .eventTitle(eventUpdateForm.getEventTitle())
                .eventContent(eventUpdateForm.getEventContent())
                .startDate(eventUpdateForm.getStartDate())
                .endDate(eventUpdateForm.getEndDate())
                .calendarGroupId(eventUpdateForm.getCalendarGroupId())
                .build();
    }

    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventException(EventErrorCode.NOT_FOUND_EVENT)
        );
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        try {
            eventRepository.delete(event);
        } catch (Exception e) {
            throw new EventException(EventErrorCode.TRANSACTION_ERROR);
        }
    }

    /**
     * 기간 내의 개인 일정 조회
     *
     * @param startDate
     * @param endDate
     * @param userId
     * @return
     */
    public List<EventListOutputSpec> getEventListByMonth(Long calendarGroupId, LocalDate startDate, LocalDate endDate, Long userId) {
        List<EventListOutputSpec> eventListOutputSpecList = new ArrayList<>();
        Optional<List<Event>> byMonthAndUserId = eventRepository.findEventsByMonthAndUserId(startDate, endDate, userId, calendarGroupId);

        if (byMonthAndUserId.isPresent()) {
            for (Event event : byMonthAndUserId.get()) {
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
        return eventListOutputSpecList;
    }

    public List<EventListOutputSpec> getEventListByDate(LocalDateTime targetDate, Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );
        List<EventListOutputSpec> eventListOutputSpecList = null;

        Optional<List<Event>> result = eventRepository.findByTargetDate(targetDate, userId);

        if (result.isPresent()) {
            eventListOutputSpecList = new ArrayList<>();

            for (Event event : result.get()) {
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
        return eventListOutputSpecList;

    }


}
