package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.UserDetail

interface GetUserStoreUseCase {
    suspend operator fun invoke(): UserDetail?
}