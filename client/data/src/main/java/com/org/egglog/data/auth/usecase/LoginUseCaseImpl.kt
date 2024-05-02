package com.org.egglog.data.auth.usecase

import com.org.egglog.domain.auth.usecase.LoginUseCase
import com.org.egglog.data.auth.service.UserService
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val userService: UserService
): LoginUseCase {
    override suspend fun invoke(id: String, password: String): Result<String> {
        TODO("Not yet implemented")
    }

}