package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Refresh
import com.org.egglog.domain.auth.model.UserParam

interface GetLoginUseCase {
    suspend operator fun invoke(provider: String, userParam: UserParam): Result<Refresh?>
}

