package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.UserDetail

interface SetUserStoreUseCase {
    suspend operator fun invoke(user: UserDetail?)
}