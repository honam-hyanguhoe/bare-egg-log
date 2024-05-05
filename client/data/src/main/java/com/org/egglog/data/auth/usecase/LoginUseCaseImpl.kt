package com.org.egglog.data.auth.usecase

import android.util.Log
import com.org.egglog.data.auth.model.UserRequest
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.model.UserParam
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val userService: UserService
): LoginUseCase {
    override suspend fun invoke(provider: String, userParam: UserParam): Result<Refresh?> = kotlin.runCatching {
        val requestBody = UserRequest(userParam.name, userParam.email, userParam.profileImgUrl).toRequestBody()
        userService.login(provider, requestBody).dataBody?.toDomainModel()
    }
}