package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.GroupDuty
import com.org.egglog.domain.group.model.GroupInfo

interface GetGroupDutyUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId : Long,
        date : String
    ): Result<GroupDuty?>
}