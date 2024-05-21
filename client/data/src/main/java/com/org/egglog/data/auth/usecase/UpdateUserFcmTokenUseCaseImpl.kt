package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.model.UserFcmTokenRequest
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserFcmTokenParam
import com.org.egglog.domain.auth.usecase.UpdateUserFcmTokenUseCase
import javax.inject.Inject

class UpdateUserFcmTokenUseCaseImpl @Inject constructor(
    private val userService: UserService
): UpdateUserFcmTokenUseCase {
    override suspend fun invoke(accessToken: String, userFcmTokenParam: UserFcmTokenParam): Result<UserDetail?> = kotlin.runCatching {
        val requestBody = UserFcmTokenRequest(fcmToken = userFcmTokenParam.fcmToken).toRequestBody()
        userService.updateFcmToken(accessToken, requestBody).dataBody?.toDomainModel()
    }
}