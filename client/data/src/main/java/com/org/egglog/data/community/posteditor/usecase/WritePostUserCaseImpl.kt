package com.org.egglog.data.community.posteditor.usecase

import android.content.Context
import android.util.Log
import com.org.egglog.data.community.posteditor.model.WritePostParam
import com.org.egglog.data.community.posteditor.service.PostEditorService
import com.org.egglog.data.datastore.UserDataStore
import com.org.egglog.domain.community.posteditor.usecase.ImageUploadUseCase
import com.org.egglog.domain.community.posteditor.usecase.WritePostUseCase
import javax.inject.Inject

class WritePostUserCaseImpl @Inject constructor(
    private val context: Context,
    private val postingService: PostEditorService,
    private val imageUploadUseCase: ImageUploadUseCase,
) : WritePostUseCase {

    override suspend fun invoke(
        accessToken: String,
        boardTitle: String,
        boardContent: String,
        uploadImages: List<ByteArray>,
        tempNickname: String,
        groupId: Int?,
        hospitalId: Int?
    ): Result<Boolean> = kotlin.runCatching {
        // firebase 이미지 업로드 로직
        var result: List<String>? = emptyList()
        if(uploadImages.isNotEmpty()) {
            result = imageUploadUseCase.uploadImage(uploadImages, "board").getOrNull()
        }

        val urls: List<String>? = result
        Log.d("커뮤니티", "uploadImage firebase return $urls")

        val requestParam = WritePostParam(
            boardTitle = boardTitle,
            boardContent = boardContent,
            pictureOne = urls?.getOrNull(0) ?: "",
            pictureTwo = urls?.getOrNull(1) ?: "",
            pictureThree = urls?.getOrNull(2) ?: "",
            pictureFour = urls?.getOrNull(3) ?: "",
            tempNickname = tempNickname,
            groupId = groupId,
            hospitalId = hospitalId
        )

        Log.d("커뮤니티", requestParam.toString())

        val requestBody = requestParam.toRequestBody()

        val response = postingService.writePost(accessToken, requestBody)
        Log.d("커뮤니티", "WritePostUserCaseImpl response ${response.dataHeader.successCode}")
        if (response.dataHeader.successCode == 0) {
            Log.d("커뮤니티", "WritePostUserCaseImpl response $response")
            true
        } else {
            throw Exception("커뮤니티 writePost ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")        }
    }
}