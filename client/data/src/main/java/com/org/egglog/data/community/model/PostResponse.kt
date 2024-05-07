package com.org.egglog.data.community.model

import com.org.egglog.domain.community.model.PostData
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse (
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

fun PostResponse.toDomainModel(): PostData {
    return PostData(
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