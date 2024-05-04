package com.org.egglog.domain.auth.usecase

interface LoginUseCase {
    suspend operator fun invoke(
        id: String,
        password: String
    ): Result<String>
}
