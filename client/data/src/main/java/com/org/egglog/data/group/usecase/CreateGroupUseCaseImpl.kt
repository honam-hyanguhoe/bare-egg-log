package com.org.egglog.data.group.usecase

import android.util.Log
import androidx.annotation.Nullable
import com.org.egglog.data.group.model.CreateGroupParam
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.domain.group.usecase.CreateGroupUseCase
import javax.inject.Inject

class CreateGroupUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : CreateGroupUseCase {
    override suspend fun invoke(
        accessToken: String, groupName: String, groupPassword: String, groupImage: Int
    ): Result<String?> = kotlin.runCatching {

        val requestParam= CreateGroupParam(
            groupName = groupName, groupPassword = groupPassword, groupImage = groupImage
        )

        Log.d("write group", "param ${requestParam.toString()}")


        val response = groupService.createGroup(accessToken, requestParam.toRequestBody())

        response.dataBody
    }
}