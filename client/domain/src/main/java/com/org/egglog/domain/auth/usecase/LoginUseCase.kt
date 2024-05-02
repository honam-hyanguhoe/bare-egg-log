package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Refresh

interface LoginUseCase {
    suspend operator fun invoke(type: String): Result<Refresh?>
}