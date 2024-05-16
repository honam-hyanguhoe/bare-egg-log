package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.GroupInfo

interface UpdateGroupInfoUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId : Long,
        newName : String,
        newPassword : String
    ): Result<GroupInfo>

}