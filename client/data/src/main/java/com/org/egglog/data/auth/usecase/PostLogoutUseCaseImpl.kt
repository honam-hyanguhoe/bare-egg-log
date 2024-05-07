package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.service.AuthService
import com.org.egglog.domain.auth.usecase.PostLogoutUseCase
import javax.inject.Inject

class PostLogoutUseCaseImpl @Inject constructor(
    private val authService: AuthService
) : PostLogoutUseCase {
    override suspend fun invoke(accessToken: String): Unit? {
        return authService.logout(accessToken).dataBody
    }
}