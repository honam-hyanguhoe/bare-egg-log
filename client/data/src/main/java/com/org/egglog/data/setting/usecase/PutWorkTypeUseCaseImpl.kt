package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.WorkTypeModifyRequest
import com.org.egglog.data.setting.model.WorkTypeRequest
import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.model.WorkTypeModifyParam
import com.org.egglog.domain.setting.model.WorkTypeParam
import com.org.egglog.domain.setting.usecase.PostWorkTypeUseCase
import com.org.egglog.domain.setting.usecase.PutWorkTypeUseCase
import javax.inject.Inject

class PutWorkTypeUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): PutWorkTypeUseCase {
    override suspend fun invoke(accessToken: String, workTypeModifyParam: WorkTypeModifyParam): Result<WorkType?> = kotlin.runCatching {
        val requestBody = WorkTypeModifyRequest(
            workTypeId = workTypeModifyParam.workTypeId,
            title = workTypeModifyParam.title,
            color = workTypeModifyParam.color,
            workTypeImgUrl = workTypeModifyParam.workTypeImgUrl,
            startTime = workTypeModifyParam.startTime,
            workTime = workTypeModifyParam.workTime
        ).toRequestBody()
        settingService.putWorkType(accessToken, requestBody).dataBody?.toDomainModel()
    }
}