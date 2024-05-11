package com.org.egglog.data.community.usecase

import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.usecase.CreateLikeUseCase
import com.org.egglog.domain.community.usecase.DeleteLikeUseCase
import javax.inject.Inject

class DeleteLikeUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): DeleteLikeUseCase {
    override suspend fun invoke(accessToken: String, boardId: Int): Result<Boolean> = kotlin.runCatching {
        val response = communityService.deleteLike(accessToken, boardId).dataHeader

        if (response.successCode == 0) {
            true
        } else {
            throw Exception("좋아요 삽입 처리 에러 발생 ${response.resultCode} : ${response.resultMessage}")
        }
    }
}