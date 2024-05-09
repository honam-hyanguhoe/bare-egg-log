package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserModifyParam

interface UpdateUserModifyUseCase {
    suspend operator fun invoke(accessToken: String, userModifyParam: UserModifyParam): Result<UserDetail?>
}