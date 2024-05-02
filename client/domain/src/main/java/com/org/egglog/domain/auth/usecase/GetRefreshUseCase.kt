package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Refresh

interface GetRefreshUseCase {
    suspend operator fun invoke(
        refresh: String
    ): Result<Refresh>
}