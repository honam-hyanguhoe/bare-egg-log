package com.org.egglog.data.community.usecase

import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.usecase.DeleteCommentUseCase
import javax.inject.Inject

class DeleteCommentUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): DeleteCommentUseCase {
    override suspend fun invoke(accessToken: String, commentId: Long): Result<Boolean> = kotlin.runCatching {
        val response = communityService.deleteComment(accessToken, commentId)
        if(response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("게시글 삭제 error 발생! ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }

}