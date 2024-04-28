package org.egglog.api.event.model.service;

import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserJpaRepository userJpaRepository;

    /**
     * 개인 일정 등록
     *
     * @param eventForm
     * @param userId
     */
    public void registerEvent(EventForm eventForm, Long userId) {
        User user = userJpaRepository.findById(userId).orElseThrow(
                () -> new UserException(UserErrorCode.NOT_EXISTS_USER)
        );

        Event event = Event.builder()
                .eventTitle(eventForm.getEventTitle())
                .eventContent(eventForm.getEventContent())
                .startDate(eventForm.getStartDate())
                .endDate(eventForm.getEndDate())
                .user(user)
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


}
