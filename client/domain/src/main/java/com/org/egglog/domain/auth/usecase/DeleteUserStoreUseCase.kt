package com.org.egglog.domain.auth.usecase

interface DeleteUserStoreUseCase {
    suspend operator fun invoke(): Unit
}