package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.main.model.stats.toDomainModel
import com.org.egglog.domain.group.model.Group
import com.org.egglog.domain.group.usecase.GetGroupListUseCase
import javax.inject.Inject

class GetGroupListUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : GetGroupListUseCase {
    override suspend fun invoke(accessToken: String): Result<List<Group>?> = kotlin.runCatching {
        Log.d("groupList", "accessToken ${accessToken}")
        val response = groupService.getGroupList(accessToken)

        Log.d("groupList", response.dataBody.toString())
        response.dataBody?.mapNotNull { it.toDomainModel() } ?: null
    }
}

