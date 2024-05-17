package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.Group

interface GetInvitationCodeUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId : Long
    ): Result<String?>

}
