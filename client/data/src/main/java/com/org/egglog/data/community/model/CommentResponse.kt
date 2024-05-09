package com.org.egglog.data.community.model

import com.org.egglog.domain.community.model.CommentData
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val commentId: Long,
    val commentContent: String,
    val hospitalName: String,
    val userId: Long,
    val tempNickname: String,
    val commentCreateAt: String? = "",
    val profileImgUrl: String,
    val isHospitalAuth: Boolean? = false,
    val recomments: List<CommentResponse> = emptyList()
)

fun CommentResponse.toDomainModel(): CommentData {
    return CommentData(
        commentId,
        commentContent,
        hospitalName,
        userId,
        tempNickname,
        commentCreateAt ?: "",
        profileImgUrl,
        isHospitalAuth ?: false,
        recomments.map { it.toDomainModel() }
    )
}
