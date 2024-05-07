package com.org.egglog.data.community.usecase

import androidx.paging.Pager
import androidx.paging.PagingData
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class GetPostListUseCaseImpl @Inject constructor(
//    private val communityService: CommunityService
//): GetPostListUseCase {
//    override suspend fun invoke(): Result<Flow<PagingData<PostData>>> {
//       return Pager(
//
//       )
//    }
//}