package com.org.egglog.domain.auth.usecase

import androidx.paging.PagingData
import com.org.egglog.domain.auth.model.HospitalParam
import com.org.egglog.domain.auth.model.UserHospital
import kotlinx.coroutines.flow.Flow

interface GetAllHospitalUseCase {
    suspend operator fun invoke(hospitalParam: HospitalParam): Result<Flow<PagingData<UserHospital>>?>
}

