package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.model.TokenResponse
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.domain.auth.usecase.PostRefreshUseCase
import com.org.egglog.data.auth.service.AuthService
import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.model.Token
import javax.inject.Inject

class PostRefreshUseCaseImpl @Inject constructor(
    private val authService: AuthService
): PostRefreshUseCase {
    override suspend fun invoke(refreshToken: String): Result<Token?> = kotlin.runCatching {
        authService.refresh(refreshToken = refreshToken).dataBody?.toDomainModel()
    }
}