package com.org.egglog.domain.community.model

data class PostDetailData(
    val boardId: Long,
    val boardTitle: String,
    val boardContent: String,
    val boardCreatedAt: String,
    val pictureOne: String ?= null,
    val pictureTwo: String ?= null,
    val pictureThree: String ?= null,
    val pictureFour: String ?= null,
    val groupId: Int ?= null,
    val userId: Int,
    val tempNickname: String ?= "익명의 구운란",
    val profileImgUrl: String,
    val commentCount: Long,
    val viewCount: Long,
    val boardLikeCount: Long,
    val hospitalName: String? = "",
    val isLiked: Boolean,
    val isCommented: Boolean,
    val isHospitalAuth: Boolean,
)