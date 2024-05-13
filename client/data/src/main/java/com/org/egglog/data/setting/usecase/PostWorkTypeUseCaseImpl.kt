package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.WorkTypeRequest
import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.model.WorkTypeParam
import com.org.egglog.domain.setting.usecase.PostWorkTypeUseCase
import javax.inject.Inject

class PostWorkTypeUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): PostWorkTypeUseCase {
    override suspend fun invoke(accessToken: String, workTypeParam: WorkTypeParam): Result<WorkType?> = kotlin.runCatching {
        val requestBody = WorkTypeRequest(
            title = workTypeParam.title,
            color = workTypeParam.color,
            workTypeImgUrl = workTypeParam.workTypeImgUrl,
            startTime = workTypeParam.startTime,
            workTime = workTypeParam.workTime
        ).toRequestBody()
        settingService.postWorkType(accessToken, requestBody).dataBody?.toDomainModel()
    }
}