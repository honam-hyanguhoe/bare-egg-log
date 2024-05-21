package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.WorkType

interface GetWorkTypeListUseCase {
    suspend operator fun invoke(accessToken: String): Result<List<WorkType>?>
}