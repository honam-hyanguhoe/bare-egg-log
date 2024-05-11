package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.Group

interface CreateGroupUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupName: String,
        groupPassword: String,
        groupImage: Int
    ): Result<String?>

}
