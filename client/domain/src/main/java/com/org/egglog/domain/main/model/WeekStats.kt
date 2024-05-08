package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class WeekStats(
    val week : Int,
    val data : WeekData
)


@Serializable
data class WeekData(
    val DAY : Int,
    val EVE : Int,
    val NIGHT : Int,
    val OFF : Int,
    val EDU : Int,
)
