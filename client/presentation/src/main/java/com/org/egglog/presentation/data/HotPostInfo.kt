package com.org.egglog.presentation.data

import com.org.egglog.domain.community.model.HotPostData
import com.org.egglog.domain.community.model.PostData
import javax.annotation.concurrent.Immutable

@Immutable
data class HotPostInfo(
    val postId: Long,
    val title: String,
    val name: String? = "익명의 구운란",
    val likeCnt: Long,
    val commentCnt: Long,
    val isLiked: Boolean
)

fun HotPostData.toUiModel(): HotPostInfo {
    return HotPostInfo(
        postId = this.boardId,
        title = this.boardTitle,
        name = this.tempNickname,
        likeCnt = this.likeCount,
        commentCnt = this.commentCount,
        isLiked = this.isLiked
    )
}