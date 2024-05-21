package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.usecase.DeleteMemberUseCase
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import javax.inject.Inject

class DeleteMemberUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : DeleteMemberUseCase {
    override suspend fun invoke(
        accessToken: String,
        groupId: Long,
        memberId: Long
    ): Result<String> = kotlin.runCatching {

        val response = groupService.deleteMember(accessToken, groupId, memberId)

        response.dataBody.toString()
    }

}

