package com.org.egglog.client.data

import com.org.egglog.domain.community.model.CommentData
import java.time.LocalDateTime

data class CommentInfo (val userId: Long, val commentId: Long, val commentContent: String, val hospitalName: String, val tempNickname: String, val commentCreateAt: String, val profileImgUrl: String, val isHospitalAuth: Boolean, val recomment: List<CommentInfo> = emptyList())

fun CommentData.toUiModel(): CommentInfo {
    return CommentInfo(
        userId = this.userId,
        commentId = this.commentId,
        commentContent = this.commentContent,
        hospitalName = this.hospitalName,
        tempNickname = this.tempNickname,
        commentCreateAt = this.commentCreateAt,
        profileImgUrl = this.profileImgUrl,
        isHospitalAuth = this.isHospitalAuth,
        recomment = this.recomment.map { it.toUiModel() }
    )
}