package com.org.egglog.data.group.usecase

import android.util.Log
import androidx.annotation.Nullable
import com.org.egglog.data.group.model.CreateGroupParam
import com.org.egglog.data.group.model.InvitationParam
import com.org.egglog.data.group.model.toDomainModel
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.domain.group.model.GroupDuty
import com.org.egglog.domain.group.usecase.CreateGroupUseCase
import com.org.egglog.domain.group.usecase.GetGroupDutyUseCase
import com.org.egglog.domain.group.usecase.GetGroupInfoUseCase
import com.org.egglog.domain.group.usecase.InviteMemberUseCase
import javax.inject.Inject

class InviteMemberUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : InviteMemberUseCase {
    override suspend fun invoke(
        accessToken: String,
        invitationCode: String,
        password: String
    ): Result<String?> = kotlin.runCatching {

        val requestParam = InvitationParam(
            invitationCode, password
        )

        val response = groupService.inviteMember(accessToken, requestParam.toRequestBody())

        response.dataBody.toString()
    }

}