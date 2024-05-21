package com.org.egglog.data.community.posteditor.usecase

import android.util.Log
import com.org.egglog.data.community.posteditor.model.ModifyPostParam
import com.org.egglog.data.community.posteditor.model.WritePostParam
import com.org.egglog.data.community.posteditor.service.PostEditorService
import javax.inject.Inject
import com.org.egglog.data.community.service.CommunityService
import com.org.egglog.domain.community.posteditor.usecase.ImageUploadUseCase
import com.org.egglog.domain.community.posteditor.usecase.ModifyPostUseCase

class ModifyPostUseCaseImpl @Inject constructor(
    private val postingService: PostEditorService,
    private val imageUploadUseCase: ImageUploadUseCase,
): ModifyPostUseCase {
    override suspend fun invoke(
        accessToken: String,
        boardId: Int,
        boardTitle: String,
        boardContent: String,
        uploadImages: List<ByteArray>
    ): Result<Boolean> = kotlin.runCatching {

        var result: List<String>? = emptyList()
        if(uploadImages.isNotEmpty()) {
            result = imageUploadUseCase.uploadImage(uploadImages, "board").getOrNull()
        }

        val urls: List<String>? = result
        val requestBody = ModifyPostParam(
            boardTitle = boardTitle,
            boardContent = boardContent,
            pictureOne = urls?.getOrNull(0) ?: "",
            pictureTwo = urls?.getOrNull(1) ?: "",
            pictureThree = urls?.getOrNull(2) ?: "",
            pictureFour = urls?.getOrNull(3) ?: "",
        ).toRequestBody()

        Log.e("ModifyPost", "수정 결과 ${postingService.modifyPost(accessToken, boardId, requestBody)}")
        val response = postingService.modifyPost(accessToken, boardId, requestBody)

        if(response.dataHeader.successCode == 0) {
            true
        } else {
            throw Exception("게시글 수정 오류 발생! ${response.dataHeader.resultCode}: ${response.dataHeader.resultMessage}")
        }


    }
}