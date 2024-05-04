package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Refresh

interface GetGoogleUseCase {
    suspend operator fun invoke(): Result<Refresh>
}