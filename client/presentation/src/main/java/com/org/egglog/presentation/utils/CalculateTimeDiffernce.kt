package com.org.egglog.presentation.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun CalculateTimeDifference(timeString: String): String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val parsedTime = LocalDateTime.parse(timeString, formatter)

    val currentTime = LocalDateTime.now()
    val differenceMinutes = ChronoUnit.MINUTES.between(parsedTime, currentTime)
    val differenceHours = ChronoUnit.HOURS.between(parsedTime, currentTime)
    val differenceDays = ChronoUnit.DAYS.between(parsedTime, currentTime)
    val differenceWeeks = ChronoUnit.WEEKS.between(parsedTime, currentTime)
    val differenceMonths = ChronoUnit.MONTHS.between(parsedTime, currentTime)

    return when {
        differenceMinutes < 1 -> "방금 전"
        differenceMinutes < 60 -> "${differenceMinutes}분 전"
        differenceHours < 24 -> "${differenceHours}시간 전"
        differenceDays < 7 -> "${differenceDays}일 전"
        differenceWeeks < 4 -> "${differenceWeeks}주 전"
        else -> "${differenceMonths}개월 전"
    }
}