package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Group

data class GroupResponse(
    val id: Long,
    val master: String,
    val name: String,
    val memberCount: Int,
    val image: Int,
)

fun GroupResponse.toDomainModel() : Group{
    return Group(
        id, master, name, memberCount, image
    )
}

