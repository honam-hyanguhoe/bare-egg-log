package com.org.egglog.domain.community.usecase

import androidx.paging.PagingData
import com.org.egglog.domain.community.model.PostData
import kotlinx.coroutines.flow.Flow

interface GetHotPostListUseCase {

    suspend operator fun invoke(accessToken: String): Result<List<PostData>>
}