package com.org.egglog.domain.setting.usecase

import com.org.egglog.domain.setting.model.AskParam


interface PostAskUseCase {
    suspend operator fun invoke(accessToken: String, askParam: AskParam): Result<Unit?>
}