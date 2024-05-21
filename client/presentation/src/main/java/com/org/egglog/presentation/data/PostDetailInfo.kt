package com.org.egglog.presentation.data

import com.org.egglog.domain.community.model.PostDetailData


data class PostDetailInfo(
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
    var isLiked: Boolean,
    val isCommented: Boolean,
    val isHospitalAuth: Boolean,
)

fun PostDetailData.toUiModel(): PostDetailInfo{
    return PostDetailInfo(
        boardId, boardTitle, boardContent, boardCreatedAt, pictureOne, pictureTwo, pictureThree, pictureFour, groupId, userId, tempNickname, profileImgUrl, commentCount, viewCount, boardLikeCount, hospitalName?: "", isLiked, isCommented, isHospitalAuth
    )
}