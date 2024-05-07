package com.org.egglog.data.community.usecase

import android.util.Log
import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.HotPostData
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import javax.inject.Inject

class GetHotPostListUseCaseImpl @Inject constructor(
   private val communityService: CommunityService
): GetHotPostListUseCase {

    override suspend fun invoke(accessToken: String): Result<List<HotPostData?>> = kotlin.runCatching {
        communityService.getHotPostList(accessToken).dataBody!!.map {it?.toDomainModel()}
    }
}