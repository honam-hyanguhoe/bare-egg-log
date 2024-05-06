package com.org.egglog.domain.auth.usecase

import com.org.egglog.domain.auth.model.AddUserParam
import com.org.egglog.domain.auth.model.UserDetail

interface UpdateUserJoinUseCase {
    suspend operator fun invoke(addUserParam: AddUserParam): Result<UserDetail?>
}