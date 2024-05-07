package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Refresh

interface PostRefreshUseCase {
    suspend operator fun invoke(
        refreshToken: String
    ): Result<Refresh?>
}