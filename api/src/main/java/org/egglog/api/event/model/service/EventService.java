package org.egglog.api.event.model.service;

import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.event.model.dto.params.EventForm;
import org.egglog.api.event.model.dto.params.EventUpdateForm;
import org.egglog.api.event.model.dto.response.EventListOutputSpec;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<EventListOutputSpec> getEventListByMonth(Long calendarGroupId, LocalDateTime startDate, LocalDateTime endDate, Long userId) {
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
//    /**
//     * 해당 달의 개인 일정 조회
//     *
//     * @param date
//     * @param userId
//     */
//    public List<EventListOutputSpec> getEventListByMonth(LocalDateTime date, Long userId) {
//        User user = userJpaRepository.findById(userId).orElseThrow(
//                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
//        );
//        List<EventListOutputSpec> eventListOutputSpecList = null;
//
//        YearMonth targetMonth = YearMonth.of(date.getYear(), date.getMonth());  //해당 년도, 월
//
//        LocalDate firstDayOfTargetMonth = targetMonth.atDay(1);
//        LocalDate lastDayOfTargetMonth = targetMonth.atEndOfMonth();
//
//        // 해당 달의 첫째 날의 이전 주의 일요일
//        LocalDateTime startOfCalendar = LocalDateTime.of(firstDayOfTargetMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)), LocalTime.MIN);
//
//        // 해당 달의 마지막 날의 다음 주의 토요일
//        LocalDateTime endOfCalendar = LocalDateTime.of(lastDayOfTargetMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)), LocalTime.MAX);
//
//        Optional<List<Event>> result = eventRepository.findByMonthAndUserId(startOfCalendar, endOfCalendar, userId);
//
//        if (result.isPresent()) {
//            eventListOutputSpecList = new ArrayList<>();
//
//            for (Event event : result.get()) {
//                EventListOutputSpec eventListOutputSpec = EventListOutputSpec.builder()
//                        .eventId(event.getId())
//                        .eventTitle(event.getEventTitle())
//                        .eventContent(event.getEventContent())
//                        .startDate(event.getStartDate())
//                        .endDate(event.getEndDate())
//                        .calendarGroupId(event.getCalendarGroup().getId())
//                        .build();
//
//                eventListOutputSpecList.add(eventListOutputSpec);
//            }
//        }
//        return eventListOutputSpecList;
//    }

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
