package com.org.egglog.data.community.usecase

import com.org.egglog.data.community.model.CreateLikeRequestParam
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.usecase.CreateLikeUseCase
import javax.inject.Inject

class CreateLikeUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): CreateLikeUseCase {
    override suspend fun invoke(accessToken: String, boardId: Int): Result<Boolean> = kotlin.runCatching {
        val requestParam = CreateLikeRequestParam(boardId).toRequestBody()
        val response = communityService.createLike(accessToken, requestParam).dataHeader
        if (response.successCode == 0) {
            true
        } else {
            throw Exception("좋아요 삽입 처리 에러 발생 ${response.resultCode} : ${response.resultMessage}")
        }
    }

}