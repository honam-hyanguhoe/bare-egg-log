package com.org.egglog.data.community.posteditor.usecase

import android.content.Context
import android.util.Log
import com.org.egglog.data.community.posteditor.model.WritePostParam
import com.org.egglog.data.community.posteditor.service.PostEditorService
import com.org.egglog.domain.community.posteditor.usecase.ImageUploadUseCase
import com.org.egglog.domain.community.posteditor.usecase.WritePostUseCase
import javax.inject.Inject

class WritePostUserCaseImpl @Inject constructor(
    private val context: Context,
    private val postingService: PostEditorService,
    private val imageUploadUseCase: ImageUploadUseCase
) : WritePostUseCase {
    override suspend fun invoke(
        boardTitle: String,
        boardContent: String,
//        pictureOne : String,
//        pictureTwo : String,
//        pictureThree : String,
//        pictureFour : String,
        uploadImages: List<ByteArray>
    ): Result<Boolean> = kotlin.runCatching {
//        imageData: List<ByteArray>, filePath: String
//        val imageData = listOf<ByteArray>()

        // firebase 이미지 업로드 로직
        val result = imageUploadUseCase.uploadImage(uploadImages, "board")
        val urls: List<String>? = result.getOrNull()
        Log.d("커뮤니티", "uploadImage firebase return $urls")

        val requestParam = WritePostParam(
            boardTitle = boardTitle,
            boardContent = boardContent,
            pictureOne = urls?.getOrNull(0) ?: "",
            pictureTwo = urls?.getOrNull(1) ?: "",
            pictureThree = urls?.getOrNull(2) ?: "",
            pictureFour = urls?.getOrNull(3) ?: ""
        )
        Log.d("커뮤니티", "${requestParam.toString()}")

        val requestBody = requestParam.toRequestBody()
        val response = postingService.writePost(requestBody)

        if (response.dataHeader.resultCode.equals(200)) {
            Log.d("커뮤니티", "WritePostUserCaseImpl response $response")
            true
        } else {
            throw Exception("커뮤니티 writePost ${response.dataHeader.resultCode} : ${response.dataHeader.resultMessage}")
        }
    }
}