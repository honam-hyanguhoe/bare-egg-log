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
        communityService.getPostDetail(accessToken, postId).dataBody!!.toDomainModel()
    }

}