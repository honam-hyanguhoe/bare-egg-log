package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.domain.group.model.DutyTag
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import com.org.egglog.domain.group.usecase.getDutyTagUseCase
import javax.inject.Inject

class GetDutyTagUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : getDutyTagUseCase {
    override suspend fun invoke(accessToken: String, groupId: Long): Result<DutyTag?> = kotlin.runCatching {
        val response = groupService.getDutyTag(accessToken, groupId)

        response.dataBody?.toDomainModel()
    }
}

