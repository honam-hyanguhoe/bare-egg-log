package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.domain.group.model.DutyTag
import com.org.egglog.domain.group.model.GroupDuty
import com.org.egglog.domain.group.usecase.GetGroupDutyUseCase
import com.org.egglog.domain.group.usecase.GetTagUseCase
import javax.inject.Inject

class GetTagUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : GetTagUseCase {
    override suspend fun invoke(accessToken: String, groupId: Long): Result<DutyTag?> =
        kotlin.runCatching {
            val response = groupService.getDutyTag(accessToken, groupId)

            response.dataBody?.toDomainModel()
        }

}

