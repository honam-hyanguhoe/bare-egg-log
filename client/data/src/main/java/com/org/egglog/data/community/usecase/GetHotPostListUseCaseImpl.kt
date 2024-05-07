package com.org.egglog.data.community.usecase

import android.util.Log
import androidx.paging.PagingData
import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.usecase.GetHotPostListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHotPostListUseCaseImpl @Inject constructor(
   private val communityService: CommunityService
): GetHotPostListUseCase {

    override suspend fun invoke(accessToken: String): Result<List<PostData?>> = kotlin.runCatching {
        Log.e("GetHotPostListUseCaseImpl", "${communityService.getHotPostList(accessToken).dataBody}")
        Log.e("GetHotPostListUseCaseImpl", "${communityService.getHotPostList(accessToken).dataBody!!.map {it?.toDomainModel()}}")
        communityService.getHotPostList(accessToken).dataBody!!.map {it?.toDomainModel()}
    }
}