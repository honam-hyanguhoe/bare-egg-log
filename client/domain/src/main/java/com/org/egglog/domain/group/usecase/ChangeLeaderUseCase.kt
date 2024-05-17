package com.org.egglog.domain.group.usecase

interface ChangeLeaderUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId : Long,
        memberId : Long
    ): Result<String>
}