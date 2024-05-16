package com.org.egglog.presentation.component.organisms.calendars.weeklyData


import android.util.Log
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.stream.Collectors
import java.util.stream.Stream

class WeeklyDataSource {
    // 현재 날짜 반환
    val today: LocalDate
        get() {
            return LocalDate.now()
        }

    fun getStartOfMonth(year: Int, month: Int): LocalDate {
        return LocalDate.of(year, month, 1)
    }

    fun getEndOfMonth(year: Int, month: Int): LocalDate {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())
    }

    fun getStartOfWeek(date: LocalDate): LocalDate {
        return date.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
    }

    fun getEndOfWeek(date : LocalDate) : LocalDate {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
    }

    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): WeeklyUiModel {
        val firstDayOfWeek :LocalDate
        if(startDate.dayOfWeek.toString() == "SUNDAY"){
            firstDayOfWeek = startDate
        }else{
            firstDayOfWeek = startDate.minusWeeks(1).with(DayOfWeek.SUNDAY)
        }
        val endDayOfWeek = firstDayOfWeek.plusDays(7)
        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)

        return toUiModel(visibleDates, lastSelectedDate)
    }


    // 주어진 시작 날짜와 종료 날짜 사이의 모든 날짜를 리스트로 생성
    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
        return Stream.iterate(startDate) { date ->
            date.plusDays(/* daysToAdd = */ 1)
        }
                .limit(numOfDays)
                .collect(/* collector = */ Collectors.toList())
    }

    // 생성된 날짜 리스트를 받아, 이를 UI에 표시할 수 있는 모델(WeeklyUiModel)로 변환
    private fun toUiModel(
            dateList: List<LocalDate>,
            lastSelectedDate: LocalDate
    ): WeeklyUiModel {
        return WeeklyUiModel(
                selectedDate = toItemUiModel(lastSelectedDate, true),
                visibleDates = dateList.map {
                    toItemUiModel(it, it.isEqual(lastSelectedDate))
                },
        )
    }


    // 각 날짜와 그 날짜가 선택된 날짜인지 여부를 인자로 받아, 이를 WeeklyUiModel.Date 객체로 변환
    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean) = WeeklyUiModel.Date(
            isSelected = isSelectedDate,
            isToday = date.isEqual(today),
            date = date,
    )
}