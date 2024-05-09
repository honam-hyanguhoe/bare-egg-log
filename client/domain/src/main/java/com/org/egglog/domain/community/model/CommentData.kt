package com.org.egglog.domain.community.model

data class CommentData (
    val commentId: Long,
    val commentContent: String,
    val hospitalName: String,
    val userId: Long,
    val tempNickname: String,
    val commentCreateAt: String,
    val profileImgUrl: String,
    val isHospitalAuth: Boolean,
    val recomment: List<CommentData> = emptyList()
)