package com.org.egglog.domain.community.usecase

import androidx.paging.PagingData
import com.org.egglog.domain.community.model.PostData
import kotlinx.coroutines.flow.Flow


interface GetPostListUseCase {

    suspend operator fun invoke(): Result<Flow<PagingData<PostData>>>
}