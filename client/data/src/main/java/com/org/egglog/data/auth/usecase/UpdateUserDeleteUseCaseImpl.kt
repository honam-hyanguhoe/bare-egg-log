package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.UpdateUserDeleteUseCase
import javax.inject.Inject

class UpdateUserDeleteUseCaseImpl @Inject constructor(
    private val userService: UserService
): UpdateUserDeleteUseCase {
    override suspend fun invoke(accessToken: String): Result<UserDetail?> = kotlin.runCatching {
        userService.updateUserDelete(accessToken).dataBody?.toDomainModel()
    }
}