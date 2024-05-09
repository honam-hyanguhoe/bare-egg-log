package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class WeekData(
    val DAY : Int,
    val EVE : Int,
    val NIGHT : Int,
    val OFF : Int,
)