package org.egglog.api.calendar.model.service;

import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;
import net.fortuna.ical4j.util.RandomUidGenerator;
import org.egglog.api.calendar.exception.CalendarErrorCode;
import org.egglog.api.calendar.exception.CalendarException;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import org.egglog.api.calendar.model.dto.params.CalendarMonthRequest;
import org.egglog.api.calendar.model.dto.response.CalendarListResponse;
import org.egglog.api.calendargroup.exception.CalendarGroupErrorCode;
import org.egglog.api.calendargroup.exception.CalendarGroupException;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupResponse;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.event.model.dto.response.EventListOutputSpec;
import org.egglog.api.event.model.entity.Event;
import org.egglog.api.event.repository.jpa.EventRepository;
import org.egglog.api.work.model.dto.response.WorkListResponse;
import org.egglog.api.work.model.dto.response.WorkResponse;
import org.egglog.api.work.model.entity.Work;
import org.egglog.api.work.repository.jpa.WorkQueryRepositoryImpl;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;
import org.egglog.api.worktype.model.entity.WorkType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarService {
    private final Bucket bucket;

    private final EventRepository eventRepository;

    private final WorkQueryRepositoryImpl workQueryRepositoryImpl;

    private final CalendarGroupRepository calendarGroupRepository;


    public void createCalendar() {
        Calendar calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.add(ImmutableVersion.VERSION_2_0);
        calendar.add(ImmutableCalScale.GREGORIAN);

    }
    public Calendar createEmptyCalendar(){
        Calendar calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.add(ImmutableVersion.VERSION_2_0);
        calendar.add(ImmutableCalScale.GREGORIAN);

        // 크리스마스 이벤트 생성
        java.util.Calendar christmas = java.util.Calendar.getInstance();
        christmas.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
        christmas.set(java.util.Calendar.DAY_OF_MONTH, 25);
        christmas.set(java.util.Calendar.YEAR, 2023);  // 예: 2023년 크리스마스
        christmas.set(java.util.Calendar.HOUR_OF_DAY, 0);
        christmas.set(java.util.Calendar.MINUTE, 0);
        christmas.set(java.util.Calendar.SECOND, 0);
        log.warn("here1");
        Date startChristmas = christmas.getTime();
        VEvent christmasEvent = new VEvent(startChristmas.toInstant().atZone(ZoneId.systemDefault()), "Christmas Day");
        log.warn("here2");

        // 이벤트에 UID 추가
        Uid uid = new RandomUidGenerator().generateUid();
        christmasEvent.getProperties().add(uid);


        // 캘린더에 이벤트 추가
        calendar.getComponents().add(christmasEvent);

        return calendar;
    }
//    public Calendar getCalendar() {
//
//    }
    public String getIcsLink(User user, Long calendarGroupId) {
        //TODO data query
        String blob = "/ics/" + user.getId() + "/" + calendarGroupId + "/calendar.ics";
        Calendar calendar = createEmptyCalendar();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            new CalendarOutputter().output(calendar, outputStream);
            // 파일을 바이트 배열로 변환
            byte[] bytes = outputStream.toByteArray();
            if (bucket.get(blob) != null) {
                bucket.get(blob).delete();
            }
            // Firebase Storage에 파일 업로드
            bucket.create(blob, bytes, "text/calendar");
        } catch (IOException e) {
            log.warn("here");
            throw new CalendarException(CalendarErrorCode.DATABASE_CONNECTION_FAILED);
        }
        return blob;
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

        List<Work> workList = workQueryRepositoryImpl.findWorkListWithWorkTypeByTime(calendarMonthRequest.getCalendarGroupId(), startDate.toLocalDate(), endDate.toLocalDate());
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

    public void updateIcs(Long userId) {

    }
}
