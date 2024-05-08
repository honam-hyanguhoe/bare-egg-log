package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkType(
    val workTypeId : Int,
    val title : String,
    val color : String,
    val workTypeImgUrl : String,
    val workTag : String,
    val startTime : String,
    val workTime : String,
)

