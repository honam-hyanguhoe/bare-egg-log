package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Token

interface PostRefreshUseCase {
    suspend operator fun invoke(
        refreshToken: String
    ): Result<Token?>
}