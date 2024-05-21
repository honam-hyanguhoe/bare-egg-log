package com.org.egglog.domain.group.model

import com.org.egglog.domain.main.model.CalendarGroup
import com.org.egglog.domain.main.model.Work
import kotlinx.serialization.Serializable

@Serializable
data class GroupMembersWork(
    val workList : List<Work>,
)

