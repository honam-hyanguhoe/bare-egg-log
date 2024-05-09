package com.org.egglog.domain.main.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class RemainDuty(
    val name : String,
    val value: Int,
    val color : String
)


//    { name: "Day", value: 0, color: "#18C5B5" },