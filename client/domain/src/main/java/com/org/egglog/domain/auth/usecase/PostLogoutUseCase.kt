package com.org.egglog.domain.auth.usecase

interface PostLogoutUseCase {
    suspend operator fun invoke(accessToken: String): Unit?
}

