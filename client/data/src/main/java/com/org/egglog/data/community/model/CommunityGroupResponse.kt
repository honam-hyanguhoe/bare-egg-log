package com.org.egglog.data.community.model

import com.org.egglog.domain.community.model.CommunityGroupData
import com.org.egglog.domain.community.model.GroupData
import kotlinx.serialization.Serializable

@Serializable
data class CommunityGroupResponse(
    val hospitalId: Int,
    val hospitalName: String,
    val groupList: List<CommunityGroup>? = null
)

fun CommunityGroupResponse.toDomainModel(): CommunityGroupData {
    return CommunityGroupData(
        hospitalId, hospitalName, groupList?.map { it.toDomainModel() } ?: emptyList()
    )
}