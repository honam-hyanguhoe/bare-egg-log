package com.org.egglog.domain.setting.usecase

interface DeleteWorkTypeUseCase {
    suspend operator fun invoke(accessToken: String, workTypeId: Long): Result<Unit?>
}