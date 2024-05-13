package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.data.main.model.toDomainModel
import com.org.egglog.domain.group.model.GroupMembersWork
import com.org.egglog.domain.group.usecase.GetMembersWorkUseCase
import com.org.egglog.domain.main.model.Work
import javax.inject.Inject

class GetMembersWorkUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : GetMembersWorkUseCase {
    override suspend fun invoke(
        accessToken: String,
        userGroupId: Long,
        targetUserId: Long,
        startDate: String,
        endDate: String
    ): Result<List<Work>?> = kotlin.runCatching {

        Log.d("getGroupWork","$userGroupId-$targetUserId-$startDate-$endDate")
        val response =
            groupService.getMembersWork(
                accessToken = accessToken,
                userGroupId = userGroupId,
                targetUserId = targetUserId,
                startDate = startDate,
                endDate = endDate
            )

        Log.d("getGroupWork", "response ${response.dataHeader.resultCode}")
        response.dataBody?.map { it.toDomainModel() }
    }
}