package com.org.egglog.component.organisms.calendars.weeklyData


import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

class WeeklyDataSource {
    val today: LocalDate
        get() {
            return LocalDate.now()
        }

    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): WeeklyUiModel {
        val firstDayOfWeek = startDate.minusWeeks(1).with(DayOfWeek.SUNDAY)
        val endDayOfWeek = firstDayOfWeek.plusDays(7)
        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toUiModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
        return Stream.iterate(startDate) { date ->
            date.plusDays(/* daysToAdd = */ 1)
        }
                .limit(numOfDays)
                .collect(/* collector = */ Collectors.toList())
    }

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

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean) = WeeklyUiModel.Date(
            isSelected = isSelectedDate,
            isToday = date.isEqual(today),
            date = date,
    )
}