package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: Long,
    val master: String,
    val name: String,
    val memberCount: Int,
    val image: Int,
)