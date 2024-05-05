package com.org.egglog.data.community.posteditor.service

interface StorageRepository {
    suspend fun uploadImage(imageDataList: List<ByteArray>, type: String): Result<List<String>>
}