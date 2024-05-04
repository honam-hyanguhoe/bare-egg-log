package com.org.egglog.domain.community.posteditor.usecase

interface WritePostUseCase {
    suspend operator fun invoke(
        boardTitle: String,
        boardContent : String,
        pictureOne : String,
        pictureTwo : String,
        pictureThree : String,
        pictureFour : String,
    ) : Result<Boolean>
}

