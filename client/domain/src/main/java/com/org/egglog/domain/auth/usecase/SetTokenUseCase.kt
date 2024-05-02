package com.org.egglog.domain.auth.usecase

interface SetTokenUseCase {
    suspend operator fun invoke(accessToken: String, refreshToken: String)
}