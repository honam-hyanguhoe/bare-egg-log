package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.GroupMembersWork
import com.org.egglog.domain.main.model.WeeklyWork
import com.org.egglog.domain.main.model.Work
import com.org.egglog.domain.main.model.WorkStats

interface GetMembersWorkUseCase {
    suspend operator fun invoke (
        accessToken: String,
        userGroupId: Long,
        targetUserId : Long,
        startDate : String,
        endDate : String
    ) : Result<List<Work>?>
}

//{
//    "userGroupId": 0,
//    "targetUserId": 0,
//    "startDate": "2024-05-13",
//    "endDate": "2024-05-13"
//}