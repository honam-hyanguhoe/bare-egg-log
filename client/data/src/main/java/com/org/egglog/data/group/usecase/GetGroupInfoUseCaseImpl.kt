package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import javax.inject.Inject

class GetGroupInfoUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : GetGroupInfoUseCase {
    override suspend fun invoke(accessToken: String, groupId: Long): Result<GroupInfo> = kotlin.runCatching{
        Log.d("groupDetail", "response $groupId")
        val response = groupService.getGroupInfo(accessToken, groupId)
        Log.d("groupDetail", "response $response")
        response.dataBody!!.toDomainModel()
    }
}

