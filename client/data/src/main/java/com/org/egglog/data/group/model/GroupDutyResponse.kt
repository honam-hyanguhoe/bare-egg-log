package com.org.egglog.data.group.model

import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupDuty
import com.org.egglog.domain.group.model.Member
import kotlinx.serialization.Serializable

@Serializable
data class GroupDutyResponse(
    val day: List<MemberDto?>,
    val eve: List<MemberDto?>,
    val night: List<MemberDto?>,
    val off: List<MemberDto?>,
    val etc: List<MemberDto?>
)

fun GroupDutyResponse.toDomainModel(): GroupDuty {
    return GroupDuty(
        day = day.mapNotNull { it?.toDomainModel() },
        eve = eve.mapNotNull { it?.toDomainModel() },
        night = night.mapNotNull { it?.toDomainModel() },
        off = off.mapNotNull { it?.toDomainModel() },
        etc = etc.mapNotNull { it?.toDomainModel() }
    )
}

