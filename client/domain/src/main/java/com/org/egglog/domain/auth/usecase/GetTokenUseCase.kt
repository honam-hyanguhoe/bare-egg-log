package com.org.egglog.domain.auth.usecase

interface GetTokenUseCase {
    suspend operator fun invoke(): Pair<String?, String?>
}