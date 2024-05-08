package com.org.egglog.presentation.data

import com.org.egglog.domain.community.model.CommunityGroupData
import com.org.egglog.domain.community.model.GroupData

data class CommunityGroupInfo (
    val hospitalId: Int,
    val hospitalName: String,
    val groupList: List<GroupInfo> ?= null
)

fun CommunityGroupData.toUiModel(): CommunityGroupInfo {
    return CommunityGroupInfo(hospitalId, hospitalName, groupList?.map { it.toUiModel() } ?: emptyList())
}
