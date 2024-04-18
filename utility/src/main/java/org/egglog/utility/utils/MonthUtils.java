package org.egglog.utility.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;


public class MonthUtils {
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Month{
        public int startDay;
        public int dateLength;
        public Integer year;
        public Integer month;
        private static Map<Integer, String> days=Map.of(
                0,"Monday",
                1,"Tuesday",
                2,"Wednesday",
                3,"Thursday",
                4,"Friday",
                5,"Saturday",
                6,"Sunday"
        );
    }

    public static Month getDates(int year, int month){
        LocalDate calendarDay=LocalDate.of(year,month,1);
        return Month.builder()
                .startDay(calendarDay.getDayOfWeek().getValue())
                .dateLength(calendarDay.lengthOfMonth())
                .year(year)
                .month(month)
                .build();
    }
}
