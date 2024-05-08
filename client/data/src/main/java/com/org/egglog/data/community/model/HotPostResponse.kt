package com.org.egglog.data.community.model

import com.org.egglog.domain.community.model.HotPostData
import kotlinx.serialization.Serializable


@Serializable
data class HotPostResponse (
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

fun HotPostResponse.toDomainModel(): HotPostData {
    return HotPostData(
        boardId = this.boardId,
        boardTitle = this.boardTitle,
        boardContent = this.boardContent,
        boardCreatedAt = this.boardCreatedAt,
        tempNickname = this.tempNickname,
        viewCount = this.viewCount,
        commentCount = this.commentCount,
        likeCount = this.likeCount,
        pictureOne = this.pictureOne,
        isLiked = this.isLiked,
        isCommented = this.isCommented,
        isHospitalAuth = this.isHospitalAuth,
        userId = this.userId,
        hospitalName = this.hospitalName
    )
}