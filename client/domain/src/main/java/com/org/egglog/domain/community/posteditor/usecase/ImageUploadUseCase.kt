package com.org.egglog.domain.community.posteditor.usecase

interface ImageUploadUseCase {
    suspend fun uploadImage(imageDataList: List<ByteArray>, type: String): Result<List<String>>

}