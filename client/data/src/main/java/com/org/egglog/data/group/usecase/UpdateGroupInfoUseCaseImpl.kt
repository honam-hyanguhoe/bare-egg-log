package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.UpdateGroupParam
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import com.org.egglog.domain.group.usecase.UpdateGroupInfoUseCase
import javax.inject.Inject

class UpdateGroupInfoUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : UpdateGroupInfoUseCase {
    override suspend fun invoke(
        accessToken: String,
        groupId: Long,
        newName: String,
        newPassword: String
    ): Result<GroupInfo> = kotlin.runCatching{
        val requestParam = UpdateGroupParam(
            newName, newPassword
        )

        val response = groupService.updateGroupInfo(accessToken, groupId, requestParam.toRequestBody())
        response.dataBody!!.toDomainModel()
    }
}

