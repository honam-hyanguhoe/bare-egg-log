package com.org.egglog.data.community.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.org.egglog.data.community.model.toDomainModel
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.model.PostData
import javax.inject.Inject

class PostListPagingSource @Inject constructor(
    private val communityService: CommunityService,
    private val accessToken: String,
    private val hospitalId: Int?,
    private val groupId: Int?,
    private val searchWord: String?,
): PagingSource<Int, PostData>() {
    override fun getRefreshKey(state: PagingState<Int, PostData>): Int? {
       return state.anchorPosition?.let { anchorPosition ->
           val anchorPage = state.closestPageToPosition(anchorPosition)
           anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
       }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostData> {
        try {
            val page: Int = params.key ?: 0
            val loadSize = params.loadSize
            val accessToken: String = accessToken
            val hospitalId: Int? = hospitalId
            val groupId: Int? = groupId
            val searchWord: String? = searchWord

            val response = communityService.getPostLIst(
                accessToken,
                hospitalId,
                groupId,
                searchWord,
                page
            )

            val size = response.dataBody!!.size
            return LoadResult.Page(
                data = response.dataBody.map { it!!.toDomainModel() },
                prevKey = null,
                nextKey = if (size == loadSize) page + 10 else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}