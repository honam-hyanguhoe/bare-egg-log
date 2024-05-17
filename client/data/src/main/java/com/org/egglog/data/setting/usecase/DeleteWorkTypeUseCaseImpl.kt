package com.org.egglog.data.setting.usecase

import android.util.Log
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.usecase.DeleteWorkTypeUseCase
import javax.inject.Inject

class DeleteWorkTypeUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): DeleteWorkTypeUseCase {
    override suspend fun invoke(accessToken: String, workTypeId: Long): Result<Unit?> = kotlin.runCatching {
        Log.e("WorkSetting", "$workTypeId")
        settingService.deleteWorkType(workTypeId = workTypeId, accessToken = accessToken).dataBody
    }
}