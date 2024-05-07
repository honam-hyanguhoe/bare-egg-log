package com.org.egglog.presentation.data

class PreviewPostInfo(
    val boardId: Long,
    val boardTitle: String,
    val boardContent: String,
    val boardCreatedAt: String,
    val tempNickname: String,
    val viewCount: Long,
    val commentCount: Long,
    val likeCount: Long,
    val pictureOne: String,
    val isLiked: Boolean,
    val isCommented: Boolean,
    val isHospitalAuth: Boolean,
    val userId: Long,
    val hospitalName: String,
)