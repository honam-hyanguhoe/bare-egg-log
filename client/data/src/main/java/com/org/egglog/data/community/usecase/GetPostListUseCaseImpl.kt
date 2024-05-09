package com.org.egglog.data.community.usecase

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.org.egglog.data.community.model.PostResponse
import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.domain.community.usecase.GetPostListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostListUseCaseImpl @Inject constructor(
    private val pagingSourceFactory: PostListPagingSourceFactory
) : GetPostListUseCase {
    override suspend fun invoke(
        accessToken: String,
        hospitalId: Int?,
        groupId: Int?,
        searchWord: String?,
    ): Result<Flow<PagingData<PostData>>?> = kotlin.runCatching {
        Pager(
            config = PagingConfig(
                pageSize = 50,
                initialLoadSize = 10,
            ),
            pagingSourceFactory = { pagingSourceFactory.create(accessToken = accessToken, hospitalId = hospitalId, groupId = groupId, searchWord = searchWord) }
        ).flow
    }

}