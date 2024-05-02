package org.egglog.api.calendar.model.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
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

import java.time.LocalDateTime;
//import java.util.TimeZone;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
     * 파일이 내부에 존재하는 경우를 가정한 예제 코드
     * @param filePath
     */
    public void readCalendarFile(String filePath){
        try {
            FileInputStream fileInputStream =new FileInputStream(filePath);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fileInputStream);
            List<CalendarComponent> events = calendar.getComponents(Component.VEVENT);
            for (CalendarComponent event : events) {
                log.info(event.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
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
    /**
     * 한달 조회
     * 개인 일정 + 근무
     *
     * @param calendarMonthRequest
     * @return
     */
    public CalendarListResponse getCalendarListByMonth(CalendarMonthRequest calendarMonthRequest, User user) {
        LocalDateTime startDate = calendarMonthRequest.getStartDate();
        LocalDateTime endDate = calendarMonthRequest.getEndDate();
        Long calendarGroupId = calendarMonthRequest.getCalendarGroupId();
        Long userId = user.getId();

        CalendarGroup calendarGroup = calendarGroupRepository.findById(calendarGroupId).orElseThrow(
                () -> new CalendarGroupException(CalendarGroupErrorCode.NOT_FOUND_CALENDAR_GROUP)
        );

        List<Work> workList = workQueryRepositoryImpl.findWorkListWithWorkTypeByTime(calendarMonthRequest.getCalendarGroupId(), startDate.toLocalDate(), endDate.toLocalDate());
        Optional<List<Event>> eventsByMonthAndUserId = eventRepository.findEventsByMonthAndUserId(startDate, endDate, userId, calendarGroupId);

        List<WorkResponse> workResponseList = new ArrayList<>();
        List<EventListOutputSpec> eventListOutputSpecList = new ArrayList<>();

        for (Work work : workList) {
            WorkType workType = work.getWorkType();
            WorkTypeResponse workTypeResponse = WorkTypeResponse.builder()
                    .workTypeId(workType.getId())
                    .workTypeImgUrl(workType.getWorkTypeImgUrl())
                    .color(workType.getColor())
                    .title(workType.getTitle())
                    .startTime(workType.getStartTime())
                    .workTime(workType.getWorkTime())
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
        log.debug("update");
        Calendar calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 4.0//EN"));
        calendar.add(ImmutableVersion.VERSION_2_0);
        calendar.add(ImmutableCalScale.GREGORIAN);
//        System.setProperty("net.fortuna.ical4j.timezone.data", "/static/asia");
        // 아시아/서울 시간대 생성
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("Asia/Seoul");
        VTimeZone tz = timezone.getVTimeZone();

        try {
            log.debug("get schedules from database");
            //사용자의 모든 근무 일정 가져오기
            List<Work> workList = workQueryRepositoryImpl.findAllWorkWithWorkTypeByUser(userId);
            //사용자의 모든 개인 일정 가져오기
            List<Event> eventList = eventRepository.findAllByUserId(userId);
            //일정이 없다면 에러 처리
            if(workList.isEmpty()&&eventList.isEmpty()){
                throw new CalendarException(CalendarErrorCode.SCHEDULE_NOT_FOUND);
            }
            //Uid 생성기
            UidGenerator ug = new RandomUidGenerator();
            //일정을 기록할 캘린더 객체 생성
            List<CalendarComponent> calendarWorkComponentList = workList.stream().map(
                    work -> {
                        //시작 시간와 근무 이름을 통해 새로운 VEvent 생성
                        VEvent workEvent = new VEvent(work.getWorkDate(),work.getWorkType().getTitle())
                                .withProperty(tz.getTimeZoneId().get())
                                        .withProperty(ug.generateUid())
                                .getFluentTarget();
                        return workEvent;
                    }).collect(Collectors.toList());
            List<CalendarComponent> calendarEventComponentList = eventList.stream()
                    .filter(event -> event.getStartDate()!=null) //start date가 존재하는 경우만 처리
                    .map(event -> {
                        if(event.getEndDate()==null){ //end date 없는 경우 처리
                            Uid uid = ug.generateUid();
                            VEvent usualEvent =  new VEvent(event.getStartDate(),event.getEventTitle())
                                    .withProperty(tz.getTimeZoneId().get())
                                    .withProperty(ug.generateUid())
                                    .getFluentTarget();
                            return usualEvent;
                        }else {
                            Uid uid = ug.generateUid();
                            VEvent usualEvent =  new VEvent(event.getStartDate(), event.getEndDate(), event.getEventTitle())
                                    .withProperty(tz.getTimeZoneId().get())
                                    .withProperty(ug.generateUid())
                                    .getFluentTarget();
                            return usualEvent;
                        }
                    }).collect(Collectors.toList());
            //캘린더 객체에 일정 추가
            calendar.getComponents().addAll(calendarWorkComponentList);
            calendar.getComponents().addAll(calendarEventComponentList);
        }catch (Exception e){
            log.warn(e.getMessage());
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
        String blob = "/ics/" + userId + "/calendar.ics";
        try {
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
}
