package com.org.egglog.domain.community.usecase

import java.awt.Image

interface WritePostUseCase {
    suspend operator fun invoke(
        boardTitle: String,
        boardContent : String,
        pictureOne : String,
        pictureTwo : String,
        pictureThree : String,
        pictureFour : String,
    )
}

