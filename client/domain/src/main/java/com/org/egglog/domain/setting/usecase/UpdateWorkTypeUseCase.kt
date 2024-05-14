package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.model.WorkTypeParam


interface UpdateWorkTypeUseCase {
    suspend operator fun invoke(accessToken: String, workTypeId: Long, workTypeParam: WorkTypeParam): Result<WorkType?>
}