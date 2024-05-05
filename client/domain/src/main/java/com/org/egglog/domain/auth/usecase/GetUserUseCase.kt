package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.UserDetail

interface GetUserUseCase {
    suspend operator fun invoke(accessToken: String): Result<UserDetail?>
}