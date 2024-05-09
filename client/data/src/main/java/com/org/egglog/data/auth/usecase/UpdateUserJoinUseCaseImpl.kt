package com.org.egglog.data.auth.usecase

import com.org.egglog.data.auth.model.AddUserRequest
import com.org.egglog.data.auth.model.toDomainModel
import com.org.egglog.data.auth.service.UserService
import com.org.egglog.domain.auth.model.AddUserParam
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.usecase.UpdateUserJoinUseCase
import javax.inject.Inject

class UpdateUserJoinUseCaseImpl @Inject constructor(
    private val userService: UserService
): UpdateUserJoinUseCase {
    override suspend fun invoke(accessToken: String, addUserParam: AddUserParam): Result<UserDetail?> = kotlin.runCatching {
        val requestBody = AddUserRequest(
            userName =  addUserParam.userName,
            hospitalId = addUserParam.hospitalId,
            empNo = addUserParam.empNo,
            fcmToken = addUserParam.fcmToken
        ).toRequestBody()
        userService.join(accessToken, requestBody).dataBody?.toDomainModel()
    }
}