package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.model.WorkTypeModifyParam
import com.org.egglog.domain.setting.model.WorkTypeParam


interface PutWorkTypeUseCase {
    suspend operator fun invoke(accessToken: String, workTypeModifyParam: WorkTypeModifyParam): Result<WorkType?>
}