package com.org.egglog.data.community.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.PostData
import javax.inject.Inject

//class CommunityPagingSource @Inject constructor(
//    private val communityService: CommunityService
//): PagingSource<Int, PostData>() {
//
//    // 새로고침 시
//    override fun getRefreshKey(state: PagingState<Int, PostData>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostData> {
//
//    }
//}