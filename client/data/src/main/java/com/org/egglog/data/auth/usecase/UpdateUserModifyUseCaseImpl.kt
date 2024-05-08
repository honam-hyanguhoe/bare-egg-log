package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.model.UserModifyRequest
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserModifyParam
import com.org.egglog.domain.auth.usecase.UpdateUserModifyUseCase
import javax.inject.Inject

class UpdateUserModifyUseCaseImpl @Inject constructor(
    private val userService: UserService
): UpdateUserModifyUseCase {
    override suspend fun invoke(accessToken: String, userModifyParam: UserModifyParam): Result<UserDetail?> = kotlin.runCatching {
        val requestBody = UserModifyRequest(userModifyParam.userName, userModifyParam.hospitalId, userModifyParam.empNo).toRequestBody()
        userService.updateUserModify(accessToken, requestBody).dataBody?.toDomainModel()
    }
}