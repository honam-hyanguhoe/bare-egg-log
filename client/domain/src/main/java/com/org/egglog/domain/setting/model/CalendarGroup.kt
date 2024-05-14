package com.org.egglog.domain.setting.model

data class CalendarGroup (
    val calendarGroupId: Long,
    val url: String?,
    val alias: String,
    var isBasic: Boolean = false,
    var isEnabled: Boolean = false
)