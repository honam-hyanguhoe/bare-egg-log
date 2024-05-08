package com.org.egglog.data.community.usecase

import android.util.Log
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.usecase.DeletePostUseCase
import javax.inject.Inject

class DeletePostUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): DeletePostUseCase {
    override suspend fun invoke(accessToken: String, postId: Int): Result<Boolean> = kotlin.runCatching {

       val response = communityService.deletePost(accessToken,postId)
        if(response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("게시글 삭제 error 발생! ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }


}