package com.org.egglog.domain.group.usecase

interface DeleteMemberUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId : Long,
        memberId : Long
    ): Result<String>
}