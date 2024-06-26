package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.model.UserRequest
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.Token
import com.org.egglog.domain.auth.model.UserParam
import com.org.egglog.domain.auth.usecase.PostLoginUseCase
import javax.inject.Inject

class PostLoginUseCaseImpl @Inject constructor(
    private val userService: UserService
): PostLoginUseCase {
    override suspend fun invoke(provider: String, authToken: String): Result<Token?> = kotlin.runCatching {
        userService.login(authToken, provider).dataBody?.toDomainModel()
    }
}