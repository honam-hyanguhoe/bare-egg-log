package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.model.WorkTypeParam


interface PostWorkTypeUseCase {
    suspend operator fun invoke(accessToken: String, workTypeParam: WorkTypeParam): Result<WorkType?>
}