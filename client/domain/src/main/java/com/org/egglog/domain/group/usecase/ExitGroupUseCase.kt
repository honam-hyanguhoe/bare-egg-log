package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.Group

interface ExitGroupUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId: Long,
    ): Result<String?>

}
