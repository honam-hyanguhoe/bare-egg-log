package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserFcmTokenParam

interface UpdateUserFcmTokenUseCase {
    suspend operator fun invoke(userFcmTokenParam: UserFcmTokenParam): Result<UserDetail?>
}