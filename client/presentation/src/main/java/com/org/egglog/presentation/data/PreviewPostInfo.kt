package com.org.egglog.presentation.data

import com.org.egglog.domain.community.model.PostData
import javax.annotation.concurrent.Immutable

@Immutable
data class PreviewPostInfo(
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

fun PostData.toUiModel(): PreviewPostInfo {
    return PreviewPostInfo(boardId, boardTitle,
        boardContent, boardCreatedAt, tempNickname, viewCount, commentCount, likeCount, pictureOne, isLiked, isCommented, isHospitalAuth, userId, hospitalName
    )
}