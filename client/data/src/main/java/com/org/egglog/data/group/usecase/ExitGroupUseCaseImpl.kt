package com.org.egglog.data.group.usecase

import com.org.egglog.data.group.service.GroupService
import com.org.egglog.domain.group.usecase.ExitGroupUseCase
import javax.inject.Inject

class ExitGroupUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : ExitGroupUseCase {
    override suspend fun invoke(accessToken: String, groupId: Long): Result<String?> = kotlin.runCatching {
        val response = groupService.exitGroup(accessToken, groupId)

        response.dataBody.toString()
    }

}