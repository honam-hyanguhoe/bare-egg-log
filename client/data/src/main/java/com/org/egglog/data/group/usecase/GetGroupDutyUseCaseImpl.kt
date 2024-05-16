package com.org.egglog.data.group.usecase

import android.util.Log
import androidx.annotation.Nullable
import com.org.egglog.data.group.model.CreateGroupParam
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.domain.group.model.GroupDuty
import com.org.egglog.domain.group.usecase.CreateGroupUseCase
import com.org.egglog.domain.group.usecase.GetGroupDutyUseCase
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import javax.inject.Inject

class GetGroupDutyUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : GetGroupDutyUseCase {
    override suspend fun invoke(accessToken: String, groupId: Long, date:String): Result<GroupDuty?> = kotlin.runCatching{
        val response = groupService.getGroupDuty(accessToken, groupId, date)
        response.dataBody?.toDomainModel()
    }
}