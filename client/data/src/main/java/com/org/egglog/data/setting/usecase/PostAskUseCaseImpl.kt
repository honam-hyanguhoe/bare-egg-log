package com.org.egglog.data.setting.usecase

import com.org.egglog.data.setting.model.AskRequest
import com.org.egglog.data.setting.service.SettingService
import com.org.egglog.domain.setting.model.AskParam
import com.org.egglog.domain.setting.usecase.PostAskUseCase
import javax.inject.Inject

class PostAskUseCaseImpl @Inject constructor(
    private val settingService: SettingService
): PostAskUseCase {
    override suspend fun invoke(accessToken: String, askParam: AskParam): Result<Unit?> = kotlin.runCatching {
        val requestBody = AskRequest(
            title = askParam.title,
            content = askParam.content,
            email = askParam.email
        ).toRequestBody()
        settingService.postAsk(accessToken, requestBody).dataBody
    }
}