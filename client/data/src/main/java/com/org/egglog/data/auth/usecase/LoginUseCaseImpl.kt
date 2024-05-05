package com.org.egglog.data.auth.usecase

import android.util.Log
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.Refresh
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val userService: UserService
): LoginUseCase {
    override suspend fun invoke(type: String): Result<Refresh?> = kotlin.runCatching {
        Log.e("response: ", type)
        val response = userService.login(type).dataBody?.toDomainModel()
        response
    }
}