package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.toDomainModel
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.domain.setting.usecase.GetWorkTypeListUseCase
import javax.inject.Inject

class GetWorkTypeListUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): GetWorkTypeListUseCase {
    override suspend fun invoke(accessToken: String): Result<List<WorkType>?> = kotlin.runCatching {
        settingService.getWorkTypeList(accessToken).dataBody?.map { it.toDomainModel() }
    }
}