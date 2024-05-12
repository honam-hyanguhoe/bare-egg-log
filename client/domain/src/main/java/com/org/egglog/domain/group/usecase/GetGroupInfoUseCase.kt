package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.GroupInfo

interface GetGroupInfoUseCase {

    suspend operator fun invoke(
        accessToken: String,
        groupId : Long
    ): Result<GroupInfo>

}