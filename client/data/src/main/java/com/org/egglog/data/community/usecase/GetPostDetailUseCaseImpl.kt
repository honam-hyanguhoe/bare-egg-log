package com.org.egglog.data.community.usecase

import android.util.Log
import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.PostDetailData
import com.org.egglog.domain.community.usecase.GetPostDetailUseCase
import javax.inject.Inject

class GetPostDetailUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): GetPostDetailUseCase {
    override suspend fun invoke(accessToken: String, postId: Int): Result<PostDetailData?> = kotlin.runCatching {
        Log.e("GetPostDetailUseCaseImpl", "${postId}의 정보를 불러오고 싶어요")
        Log.e("GetPostDetailUseCaseImpl", "${communityService.getPostDetail(accessToken, postId).dataBody!!.toDomainModel()}")
        communityService.getPostDetail(accessToken, postId).dataBody!!.toDomainModel()
    }

}