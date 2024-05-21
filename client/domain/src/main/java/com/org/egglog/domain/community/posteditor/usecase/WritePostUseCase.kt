package com.org.egglog.domain.community.posteditor.usecase

interface WritePostUseCase {
    suspend operator fun invoke(
        accessToken: String,
        boardTitle: String,
        boardContent: String,
//        pictureOne : String,
//        pictureTwo : String,
//        pictureThree : String,
//        pictureFour : String,
        uploadImages: List<ByteArray>,
        tempNickname: String,
        groupId: Int?,
        hospitalId: Int?
    ) : Result<Boolean>
}

