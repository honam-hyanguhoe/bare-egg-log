package com.org.egglog.data.auth.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.UserHospital
import dagger.assisted.Assisted
import javax.inject.Inject

class HospitalPagingSource @Inject constructor(
    private val userService: UserService,
    private val search: String
) : PagingSource<Int, UserHospital>() {
    override fun getRefreshKey(state: PagingState<Int, UserHospital>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserHospital> {
        try {
            val page: Int = params.key ?: 1
            val loadSize = params.loadSize
            val search: String = search // 검색 쿼리

            val response = userService.getHospitals(
                page = page,
                size = loadSize,
                search = search
            )

            val size = response.dataBody.size
            return LoadResult.Page(
                data = response.dataBody.map { it.toDomainModel() },
                prevKey = null,
                nextKey = if (size == loadSize) page + 1 else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}