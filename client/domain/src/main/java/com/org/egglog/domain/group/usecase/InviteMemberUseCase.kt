package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.Group

interface InviteMemberUseCase {
    suspend operator fun invoke(
        accessToken: String,
        invitationCode : String,
        password : String,
    ): Result<String?>

}
