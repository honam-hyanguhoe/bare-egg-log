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
    override suspend fun invoke(hospitalParam: HospitalParam): Result<Flow<PagingData<UserHospital>>?> = kotlin.runCatching {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { pagingSourceFactory.create(search = hospitalParam.search) },
        ).flow
    }
}