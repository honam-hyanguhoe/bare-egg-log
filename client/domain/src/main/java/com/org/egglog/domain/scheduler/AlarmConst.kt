package com.org.egglog.domain.scheduler

object AlarmConst {
    const val REQUEST_CODE = "request_code"
    const val INTERVAL_HOUR = "interval_hour"
    const val INTERVAL_MINUTE = "interval_minute"
    const val INTERVAL_SECOND = "interval_second"
    const val REPEAT_COUNT = "repeat_count"
    const val MINUTES_TO_ADD = "minutes_to_add"
    const val TARGET_DATE_YEAR = "target_date_year"
    const val TARGET_DATE_MONTH = "target_date_month"
    const val TARGET_DATE_DAY = "target_date_day"
    const val CUR_REPEAT_COUNT = "cur_repeat_count"
    const val STOP_BY_USER = "stop_by_user"

    const val requestAlarmClock = 0
    const val requestDate = 1
    const val requestRepeat = 2

    const val PENDING_REPEAT_CLOCK = 0
    const val PENDING_SINGLE_DATE = 1
    const val PENDING_REPEAT = 2
}