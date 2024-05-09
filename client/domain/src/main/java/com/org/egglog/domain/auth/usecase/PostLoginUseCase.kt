package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.Token
import com.org.egglog.domain.auth.model.UserParam

interface PostLoginUseCase {
    suspend operator fun invoke(provider: String, userParam: UserParam): Result<Token?>
}

