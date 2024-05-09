package com.org.egglog.domain.group

data class Group(
    val id: Long,
    val master: String,
    val name: String,
    val memberCount: Int,
    val image: Int,
)