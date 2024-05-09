package com.org.egglog.data.community.usecase

import android.util.Log
import com.org.egglog.data.community.model.CreateCommentRequestParam
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.usecase.CreateCommentUseCase
import javax.inject.Inject

class CreateCommentUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): CreateCommentUseCase {
        override suspend fun invoke(
        accessToken: String,
        boardId: Int,
        commentContent: String,
        parentId: Long,
        tempNickname: String
    ): Result<Boolean> = kotlin.runCatching {
        val requestParam = CreateCommentRequestParam(
            boardId, commentContent, parentId, tempNickname
        ).toRequestBody()

        val response = communityService.createComment(accessToken, requestParam)

        if (response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("커뮤니티 댓글 작성 에러 발생 ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }

}