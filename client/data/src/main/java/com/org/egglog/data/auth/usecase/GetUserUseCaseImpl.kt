package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    private val userService: UserService
): GetUserUseCase {
    override suspend fun invoke(accessToken: String): Result<UserDetail?> = kotlin.runCatching {
        userService.findUser(accessToken).dataBody?.toDomainModel()
    }
}