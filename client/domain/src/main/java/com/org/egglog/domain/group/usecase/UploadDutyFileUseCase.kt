package com.org.egglog.domain.group.usecase

import com.org.egglog.domain.group.model.GroupInfo
import com.org.egglog.domain.group.model.UploadDutyFile

interface UploadDutyFileUseCase {
    suspend operator fun invoke(
        accessToken: String,
        groupId : Long,
        dutyFileData : UploadDutyFile
    ): Result<String>
}