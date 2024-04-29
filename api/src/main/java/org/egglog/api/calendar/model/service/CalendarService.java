package org.egglog.api.calendar.model.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
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
import java.time.temporal.Temporal;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarService {
    private final Bucket bucket;

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
    public String getIcsLink(User user, Long calendarGroupId){
        //TODO data query
        String blob = "/ics/"+user.getId()+"/"+calendarGroupId+"/calendar.ics";
        Calendar calendar = createEmptyCalendar();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            new CalendarOutputter().output(calendar, outputStream);
            // 파일을 바이트 배열로 변환
            byte[] bytes = outputStream.toByteArray();
            if(bucket.get(blob) != null) {
                bucket.get(blob).delete();
            }
            // Firebase Storage에 파일 업로드
            bucket.create(blob, bytes, "text/calendar");
        }catch (IOException e){
            log.warn("here");
            throw new CalendarException(CalendarErrorCode.DATABASE_CONNECTION_FAILED);
        }
        return blob;
    }
}
