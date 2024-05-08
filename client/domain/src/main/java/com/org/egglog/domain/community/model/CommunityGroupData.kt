package com.org.egglog.domain.community.model

data class CommunityGroupData(
    val hospitalId: Int,
    val hospitalName: String,
    val groupList: List<GroupData> ?= null
)