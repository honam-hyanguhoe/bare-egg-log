package com.org.egglog.domain.auth.usecase

interface GetGoogleUseCase {
    suspend operator fun invoke(): Result<String>
}