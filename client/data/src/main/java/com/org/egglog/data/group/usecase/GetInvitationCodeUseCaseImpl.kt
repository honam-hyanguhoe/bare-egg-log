package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import com.org.egglog.domain.group.usecase.GetInvitationCodeUseCase
import javax.inject.Inject

class GetInvitationCodeUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : GetInvitationCodeUseCase {
    override suspend fun invoke(accessToken: String, groupId: Long): Result<String?> = kotlin.runCatching{
       val response = groupService.getInvitationCode(accessToken, groupId)
        response.dataBody
    }

}

