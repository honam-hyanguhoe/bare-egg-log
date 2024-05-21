package com.org.egglog.data.setting.usecase

import android.util.Log
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.usecase.GetCalendarLinkUseCase
import javax.inject.Inject

class GetCalendarLinkUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): GetCalendarLinkUseCase {
    override suspend fun invoke(accessToken: String): Result<String> = kotlin.runCatching {
        Log.e("SettingService", "${settingService.getCalendarLink(accessToken).dataBody}")
        settingService.getCalendarLink(accessToken).dataBody.toString()
    }
}