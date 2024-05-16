package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.DutyTag

interface getDutyTagUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId: Long,
    ): Result<DutyTag?>

}
