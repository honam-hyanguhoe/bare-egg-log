package com.org.egglog.data.community.usecase

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingData
import com.org.egglog.data.community.model.PostResponse
import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostListUseCaseImpl @Inject constructor(
    private val communityService: CommunityService
): GetPostListUseCase {
    //    override suspend fun invoke(): Result<Flow<PagingData<PostData>>> {
//       return Pager(
//
//       )
//    }
    override suspend fun invoke(
        accessToken: String,
        hospitalId: Int?,
        groupId: Int?,
        searchWord: String?,
        lastBoardId: Int?
    ): Result<List<PostData?>> = kotlin.runCatching {
        communityService.getPostLIst(accessToken,hospitalId,groupId,searchWord,lastBoardId).dataBody!!.map {it?.toDomainModel()}
    }

}