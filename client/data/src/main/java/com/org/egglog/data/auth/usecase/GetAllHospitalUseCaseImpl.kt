package com.org.egglog.data.auth.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.org.egglog.domain.auth.model.HospitalParam
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.domain.auth.usecase.GetAllHospitalUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHospitalUseCaseImpl @Inject constructor(
    private val pagingSourceFactory: HospitalPagingSourceFactory
): GetAllHospitalUseCase {
    override suspend fun invoke(search: String): Result<Flow<PagingData<UserHospital>>?> = kotlin.runCatching {
        Pager(
            config = PagingConfig(
                pageSize = 50,
                initialLoadSize = 50
            ),
            pagingSourceFactory = { pagingSourceFactory.create(search = search) },
        ).flow
    }
}