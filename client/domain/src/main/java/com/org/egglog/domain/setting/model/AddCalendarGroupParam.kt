package com.org.egglog.domain.setting.model

data class AddCalendarGroupParam(
    val alias: String,
    val url: String?,
    val inUrl: Boolean
)