package com.org.egglog.presentation.domain.community.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.community.posteditor.usecase.ModifyPostUseCase
import com.org.egglog.domain.community.usecase.GetPostDetailUseCase
import com.org.egglog.presentation.data.toUiModel
import com.org.egglog.presentation.domain.setting.viewmodel.CalendarSettingSideEffect
import com.org.egglog.presentation.utils.bitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ModifyPostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val modifyPostUseCase: ModifyPostUseCase
) : ViewModel(), ContainerHost<ModifyPostState, ModifyPostSideEffect> {

    private val postId: Int =
        savedStateHandle.get<Int>("postId") ?: throw IllegalArgumentException("postId is required")
    private var accessToken: String? = null

    override val container: Container<ModifyPostState, ModifyPostSideEffect> = container(
        initialState = ModifyPostState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(ModifyPostSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )

    init {
        load()
    }

    private fun load() = intent {
        val postDetailInfo = getPostDetailUseCase("Bearer ${accessToken}", postId).map {
            it!!.toUiModel()
        }.getOrThrow()

        reduce {
            state.copy(title = postDetailInfo.boardTitle, content = postDetailInfo.boardContent)
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeTitle(title: String) = blockingIntent {
        reduce {
            state.copy(title = title)
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeContent(content: String) = blockingIntent {
        reduce {
            state.copy(content = content)
        }
    }

    fun onClickPost() = intent {
        postSideEffect(ModifyPostSideEffect.Toast("수정되었습니다."))
        modifyPostUseCase(
            "Bearer $accessToken",
            postId,
            state.title,
            state.content,
            uploadImages = state.uploadImage.map { bitmap ->
                bitmapToByteArray(bitmap)
            })

    }
}

data class ModifyPostState(
    val title: String = "",
    val content: String = "",
    val uploadImage: List<Bitmap> = listOf(),
)

sealed interface ModifyPostSideEffect {
    class Toast(val message: String) : ModifyPostSideEffect
}