package com.org.egglog.domain.auth.usecase

interface DeleteTokenUseCase {
    suspend operator fun invoke(): Unit
}