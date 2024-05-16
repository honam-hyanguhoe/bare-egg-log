package org.egglog.api.calendar.model.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.Standard;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.egglog.api.calendar.exception.CalendarErrorCode;
import org.egglog.api.calendar.exception.CalendarException;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
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

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.util.TimeZone;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarService {
    private final Bucket bucket;

    private final EventRepository eventRepository;

    private final WorkQueryRepositoryImpl workQueryRepositoryImpl;

    private final CalendarGroupRepository calendarGroupRepository;



    /**
     * url or file path ics를 받아서 일정에 저장하는 서비스
     * @author 김형민
     */
    public void readCalendarFile(String path, CalendarGroup calendarGroup, User loginUser) {
        InputStream inputStream = null;
        List<Event> icsEventList = new ArrayList<>();
        if (!path.endsWith(".ics")) throw new CalendarException(CalendarErrorCode.IS_NOT_ICS);
        try {
            // URL인지 확인
            if (path.startsWith("http://") || path.startsWith("https://")) {
                URL url = new URL(path);
                inputStream = url.openStream(); // URL에서 스트림 열기
            } else {
                inputStream = new FileInputStream(path); // 파일 경로에서 스트림 열기
            }

            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(inputStream);
            List<CalendarComponent> events = calendar.getComponents(Component.VEVENT);

            for (CalendarComponent event : events) {
                Event eggLogEvent = Event.builder().calendarGroup(calendarGroup).user(loginUser).build();
                for (Property property : event.getProperties()) {
                    String name = property.getName();
                    switch (name){
                        case "SUMMARY": eggLogEvent.setEventTitle(property.getValue()); break;
                        case "UID" : eggLogEvent.setUuid(property.getValue()); break;
                        case "DTSTART" : eggLogEvent.setStartDate(icsTimeToLocalDateTime(property.getValue())); break;
                        case "DTEND" : eggLogEvent.setEndDate(icsTimeToLocalDateTime(property.getValue())); break;
                        case "DESCRIPTION" : eggLogEvent.setEventContent(property.getValue()); break;
                    }
                }
                icsEventList.add(eggLogEvent);
            }
        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.ICS_SYNC_FAIL);
        } finally {
            eventRepository.saveAll(icsEventList);
            if (inputStream != null) {
                try {
                    inputStream.close(); // 스트림 닫기
                } catch (Exception e) {
                    log.error("스트림 닫기 에러 = {}", e.getMessage());
                }
            }

        }
    }
    /**
     * 유저가 버튼을 누르면 유저의 캘린더 그룹 중에서 url이 있는 것들은 동기화를 한다.
     * @author 김형민
     */
    public void updateIcsEventsForSyncRequest(User loginUser){
        Map<CalendarGroup, Set<String>> uuidMaps = calendarGroupRepository
                .findCalendarGroupsWithEventUuids(loginUser.getId());
        for (CalendarGroup calendarGroup : uuidMaps.keySet()) {
            updateEventForUrl(calendarGroup, uuidMaps.get(calendarGroup), loginUser);
        }
    }
    private void updateEventForUrl(CalendarGroup calendarGroup, Set<String> uuidSet, User loginUser){
        String path = calendarGroup.getUrl();
        InputStream inputStream = null;
        List<Event> icsEventList = new ArrayList<>();
        if (!path.endsWith(".ics")) throw new CalendarException(CalendarErrorCode.IS_NOT_ICS);
        try {
            // URL인지 확인
            if (path.startsWith("http://") || path.startsWith("https://")) {
                URL url = new URL(path);
                inputStream = url.openStream(); // URL에서 스트림 열기
            } else {
                throw new CalendarException(CalendarErrorCode.ICS_SYNC_FAIL);
            }
            List<CalendarComponent> events = new CalendarBuilder().build(inputStream)
                    .getComponents(Component.VEVENT);

            for (CalendarComponent event : events) {
                Event eggLogEvent = Event.builder()
                        .calendarGroup(calendarGroup)
                        .user(loginUser)
                        .build();
                for (Property property : event.getProperties()) {
                    String name = property.getName();
                    switch (name){
                        case "SUMMARY": eggLogEvent.setEventTitle(property.getValue()); break;
                        case "UID" : eggLogEvent.setUuid(property.getValue()); break;
                        case "DTSTART" : eggLogEvent.setStartDate(icsTimeToLocalDateTime(property.getValue())); break;
                        case "DTEND" : eggLogEvent.setEndDate(icsTimeToLocalDateTime(property.getValue())); break;
                        case "DESCRIPTION" : eggLogEvent.setEventContent(property.getValue()); break;
                    }
                }
                if (!uuidSet.contains(eggLogEvent.getUuid())) icsEventList.add(eggLogEvent);
            }
        } catch (Exception e) {
            throw new CalendarException(CalendarErrorCode.ICS_SYNC_FAIL);
        } finally {
            eventRepository.saveAll(icsEventList);
            if (inputStream != null) {
                try {
                    inputStream.close(); // 스트림 닫기
                } catch (Exception e) {
                    log.error("스트림 닫기 에러 = {}", e.getMessage());
                }
            }

        }
    }


    private LocalDateTime icsTimeToLocalDateTime(String stringTime) throws DateTimeParseException {
        if (stringTime.length() == 16) {
            // ISO 8601 확장 포멧 ("yyyyMMdd'T'HHmmss'Z'")
            return LocalDateTime.parse(stringTime, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        } else if (stringTime.length() == 8) {
            // 연월일 포멧 ("yyyyMMdd"), 자정 시간 추가
            return LocalDateTime.parse(stringTime + "T000000", DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        } else {
            throw new DateTimeParseException("Unsupported date format", stringTime, 0);
        }
    }

    public String getIcsLink(User user) {
        //TODO data query
        String blobPath = "ics/" + user.getId() + "/calendar.ics";
//        String blobPath = "ics/" + user.getId() + "/tree.txt";
        try {
            log.debug("test");
            Blob blob = null;
            try {
                blob = bucket.get(blobPath);
                log.debug(String.valueOf(blob.exists()));
            }catch (NullPointerException e){
                log.debug("no bucket");
                updateIcs(user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CalendarException(CalendarErrorCode.DATABASE_CONNECTION_FAILED);
        }
        return blobPath;
    }

    public void updateIcs(Long userId) {
        log.debug("update");
        Calendar calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 4.0//EN"));
        calendar.add(ImmutableVersion.VERSION_2_0);
        calendar.add(ImmutableCalScale.GREGORIAN);

//        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
//        TimeZone timezone = registry.getTimeZone("Asia/Seoul");
//        VTimeZone tz = timezone.getVTimeZone();
//        calendar.getComponents().add(tz);

        try {
            log.debug("get schedules from database");
            //사용자의 모든 근무 일정 가져오기
            List<Work> workList = workQueryRepositoryImpl.findAllWorkWithWorkTypeByUser(userId);
            //사용자의 모든 개인 일정 가져오기
            List<Event> eventList = eventRepository.findAllByUserId(userId);
            //일정이 없다면 에러 처리
            log.debug("no schedules");
            if(workList.isEmpty()&&eventList.isEmpty()){
                throw new CalendarException(CalendarErrorCode.SCHEDULE_NOT_FOUND);
            }
            //Uid 생성기
            //일정을 기록할 캘린더 객체 생성
            log.debug("create..");
            List<CalendarComponent> calendarWorkComponentList = workList.stream().map(
                    work -> {
                        //시작 시간와 근무 이름을 통해 새로운 VEvent 생성
                        return new VEvent(work.getWorkDate(),work.getWorkType().getTitle())
                                .withProperty(new TzId("Asia/Seoul"))
                                .withProperty(new Uid(work.getUuid()))
                                .<VEvent>getFluentTarget();
                    }).collect(Collectors.toList());
            log.debug(calendarWorkComponentList.toString());
            List<CalendarComponent> calendarEventComponentList = eventList.stream()
                    .filter(event -> event.getStartDate()!=null) //start date가 존재하는 경우만 처리
                    .map(event -> {
                        if(event.getEndDate()==null){ //end date 없는 경우 처리
                            return new VEvent(event.getStartDate(),event.getEventTitle())
                                    .withProperty(new TzId("Asia/Seoul"))
                                    .withProperty(new Uid(event.getUuid()))
                                    .<VEvent>getFluentTarget();
                        }else {
                            return new VEvent(event.getStartDate(), event.getEndDate(), event.getEventTitle())
                                    .withProperty(new TzId("Asia/Seoul"))
                                    .withProperty(new Uid(event.getUuid()))
                                    .getFluentTarget();
                        }
                    }).collect(Collectors.toList());
            log.debug(calendarEventComponentList.toString());
            //캘린더 객체에 일정 추가
            ComponentList<CalendarComponent> components = calendar.getComponentList();
            components.addAll(calendarWorkComponentList);
            components.addAll(calendarEventComponentList);
            calendar.setComponentList(components);
            log.debug("component : {}",components.getAll().toString());
        }catch (Exception e){
            log.warn(e.getMessage());
            throw new CalendarException(CalendarErrorCode.CREATE_FAIL);
        }
        try{
            //업로드
            uploadCalendar(userId,calendar);
        }catch (Exception e){
            log.warn("업로드 실패");
        }
    }

    public void uploadCalendar(Long userId, Calendar calendar){
        //TODO data query
        String blob = "ics/" + userId + "/calendar.ics";
        try {
            log.debug("upload..");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
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
    }


//    /**
//     * 캘린더 startDate - endDate 조회
//     * 개인 일정 + 근무 일정
//     * @param calendarMonthRequest
//     * @param user
//     * @return
//     */
//    public List<CalendarListResponse> getCalendarList(CalendarMonthRequest calendarMonthRequest, User user) {
//        Map<LocalDate, CalendarListResponse> dailySchedules = new HashMap<>();
//
//        CalendarListResponse calendarListResponse = getCalendarListByMonth(calendarMonthRequest, user);
//        List<EventListOutputSpec> eventList = calendarListResponse.getEventList();
//        WorkListResponse workListResponse = calendarListResponse.getWorkList();
//
//        if (!eventList.isEmpty()) {
//            for (EventListOutputSpec event : eventList) {
//                LocalDate eventStart = event.getStartDate().toLocalDate();
//                LocalDate eventEnd = event.getEndDate().toLocalDate();
//
//                eventStart.datesUntil(eventEnd.plusDays(1)).forEach(date -> {
//                    CalendarListResponse schedule = dailySchedules.get(date);
//                    if (schedule == null) {
//                        schedule = new CalendarListResponse(date);
//                        dailySchedules.put(date, schedule);
//                    }
//                    schedule.addEvent(event);
//                });
//            }
//        }
//        if (workListResponse != null) {
//            List<WorkResponse> workList = workListResponse.getWorkList();
//            CalendarGroupResponse calendarGroup = workListResponse.getCalendarGroup();
//
//            for (WorkResponse work : workList) {
//                LocalDate date = work.getWorkDate();    //근무하는 날짜
//                CalendarListResponse schedule = dailySchedules.get(date);
//                if (schedule == null) {
//                    schedule = new CalendarListResponse(date);
//                    dailySchedules.put(date, schedule);
//                }
//                schedule.addCalendarGroup(calendarGroup);
//                schedule.addWorkToWorkList(work);
//                schedule.setCalendarGroupId(calendarGroup.getCalendarGroupId());
//            }
//        }
//        List<CalendarListResponse> result = new ArrayList<>();
//        for (CalendarListResponse value : dailySchedules.values()) {
//            result.add(value);
//        }
//        return result;
//    }

//    /**
//     * 한달 조회
//     * 개인 일정 + 근무
//     *
//     * @param calendarMonthRequest
//     * @return
//     */
//    private CalendarListResponse getCalendarListByMonth(CalendarMonthRequest calendarMonthRequest, User user) {
//        LocalDateTime startDate = calendarMonthRequest.getStartDate();
//        LocalDateTime endDate = calendarMonthRequest.getEndDate();
//        Long calendarGroupId = calendarMonthRequest.getCalendarGroupId();
//        Long userId = user.getId();
//
//        CalendarGroup calendarGroup = calendarGroupRepository.findById(calendarGroupId).orElseThrow(
//                () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
//        );
//
//        List<Work> workList = workQueryRepositoryImpl.findWorkListWithAllByTime(calendarMonthRequest.getCalendarGroupId(), startDate.toLocalDate(), endDate.toLocalDate());
//        Optional<List<Event>> eventsByMonthAndUserId = eventRepository.findEventsByMonthAndUserId(startDate, endDate, userId, calendarGroupId);
//
//        List<WorkResponse> workResponseList = new ArrayList<>();
//        List<EventListOutputSpec> eventListOutputSpecList = new ArrayList<>();
//
//        for (Work work : workList) {
//            WorkType workType = work.getWorkType();
//            WorkTypeResponse workTypeResponse = WorkTypeResponse.builder()
//                    .workTypeId(workType.getId())
//                    .workTypeImgUrl(workType.getWorkTypeImgUrl())
//                    .color(workType.getColor())
//                    .title(workType.getTitle())
//                    .startTime(workType.getStartTime())
//                    .workTime(workType.getWorkTime())
//                    .build();
//
//            WorkResponse workResponse = WorkResponse.builder()
//                    .workId(work.getId())
//                    .workDate(work.getWorkDate())
//                    .workType(workTypeResponse)
//                    .build();
//
//            workResponseList.add(workResponse);
//        }
//
//        CalendarGroupResponse calendarGroupResponse = CalendarGroupResponse.builder()
//                .calendarGroupId(calendarGroupId)
//                .url(calendarGroup.getUrl())
//                .alias(calendarGroup.getAlias())
//                .build();
//
//        WorkListResponse workListResponse = WorkListResponse.builder()
//                .workList(workResponseList)
//                .calendarGroup(calendarGroupResponse)
//                .build();
//
//        if (eventsByMonthAndUserId.isPresent()) {
//            for (Event event : eventsByMonthAndUserId.get()) {
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
//
//        return CalendarListResponse.builder()
//                .workList(workListResponse)
//                .eventList(eventListOutputSpecList)
//                .build();
//    }



}
