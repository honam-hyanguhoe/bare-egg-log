package com.org.egglog.data.group.usecase

import android.util.Log
import com.org.egglog.data.group.model.DutyFileDto
import com.org.egglog.data.group.model.UploadDutyFileParam
import com.org.egglog.data.group.service.GroupService
import com.org.egglog.domain.group.model.DutyTag
import com.org.egglog.domain.group.model.UploadDutyFile
import com.org.egglog.domain.group.usecase.ExitGroupUseCase
import com.org.egglog.domain.group.usecase.UploadDutyFileUseCase
import javax.inject.Inject

class UploadDutyFileUseCaseImpl @Inject constructor(
    private val groupService: GroupService
) : UploadDutyFileUseCase {

    //    override suspend fun invoke(
//        accessToken: String,
//        groupId: Long,
//        dutyFileData: UploadDutyFile
//    ): Result<String> = kotlin.runCatching{
//
//        val requestParam = UploadDutyFileParam(
//            date= dutyFileData.date,
//            dutyList = dutyFileData.dutyList,
//            customWorkTag = dutyFileData.customWorkTag,
//            userName = dutyFileData.userName,
//            day = dutyFileData.day
//        )
//
//        val response = groupService.uploadDutyFile(accessToken, groupId, requestParam.toRequestBody())
//
//        response.dataBody.toString()
//    }
    override suspend fun invoke(
        accessToken: String,
        groupId: Long,
        dutyFileData: UploadDutyFile
    ): Result<String> = kotlin.runCatching{

        val requestParam = UploadDutyFileParam(
            date= dutyFileData.date,
            dutyList = dutyFileData.dutyList,
            customWorkTag = dutyFileData.customWorkTag,
            userName = dutyFileData.userName,
            day = dutyFileData.day
        )

        Log.d("upload", requestParam.toString())
        val response = groupService.uploadDutyFile(accessToken, groupId, requestParam.toRequestBody())

        response.dataBody.toString()
    }

}