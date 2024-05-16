package com.org.egglog.domain.group.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupDuty(
    val day: List<Member>,
    val eve: List<Member>,
    val night: List<Member>,
    val off: List<Member>,
    val etc: List<Member>
)
