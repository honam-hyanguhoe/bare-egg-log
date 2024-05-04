package com.org.egglog.data.community.usecase

import android.content.Context
import android.util.Log
import com.org.egglog.data.community.model.WritePostParam
import com.org.egglog.data.community.service.PostingService
import com.org.egglog.domain.community.usecase.WritePostUseCase
import javax.inject.Inject

class WritePostUserCaseImpl @Inject constructor(
    private val context : Context,
    private val postingService: PostingService
) : WritePostUseCase{
    override suspend fun invoke(
        boardTitle: String,
        boardContent: String,
        pictureOne: String,
        pictureTwo: String,
        pictureThree: String,
        pictureFour: String
    ) : Result<Boolean> = kotlin.runCatching {
        val requestBody = WritePostParam(
            boardTitle,
            boardContent,
            pictureOne,
            pictureTwo,
            pictureThree,
            pictureFour
        ).toRequestBody()

        Log.d("커뮤니티", "WritePostUserCaseImpl request $requestBody")

        val response = postingService.writePost(requestBody)

        if (response.dataHeader.resultCode.equals(200)) {
            Log.d("커뮤니티", "WritePostUserCaseImpl response $response")
            true
        } else {
            throw Exception("커뮤니티 writePost ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }

}