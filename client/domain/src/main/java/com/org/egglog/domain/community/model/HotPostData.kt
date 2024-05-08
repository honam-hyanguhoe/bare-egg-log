package com.org.egglog.domain.community.model

data class HotPostData constructor(
    val boardId: Long,
    val boardTitle: String,
    val boardContent: String,
    val boardCreatedAt: String,
    val tempNickname: String? = "익명의 구운란",
    val viewCount: Long,
    val commentCount: Long,
    val likeCount: Long,
    val pictureOne: String? = null,
    val isLiked: Boolean,
    val isCommented: Boolean,
    val isHospitalAuth: Boolean,
    val userId: Long,
    val hospitalName: String,
)