package com.org.egglog.presentation.domain.community.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.community.posteditor.usecase.WritePostUseCase
import com.org.egglog.presentation.utils.bitmapToByteArray
import com.org.egglog.presentation.utils.resizeImage
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
class WritePostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTokenUseCase: GetTokenUseCase,
    private val writePostUseCase: WritePostUseCase
) : ViewModel(), ContainerHost<WritePostState, PostSideEffect> {

    private var groupId: Int? = savedStateHandle.get<Int>("groupId")
        ?: throw IllegalArgumentException("groupId is required")
    private var hospitalId: Int? = savedStateHandle.get<Int>("hospitalId")
        ?: throw IllegalArgumentException("hospitalId is required")
    private var accessToken: String? = null

    override val container: Container<WritePostState, PostSideEffect> = container(
        initialState = WritePostState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(PostSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )

    init {
        intent {
            accessToken = getTokenUseCase().first
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeTitle(value: String) = blockingIntent {
        reduce {
            state.copy(title = value)
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeContent(value: String) = blockingIntent {
        reduce {
            state.copy(content = value)
        }
    }

    fun onClickPost() = intent {
        reduce { state.copy(isLoading = true) }

        if (hospitalId == -1) {
            hospitalId = null
        }
        if (groupId == -1) {
            groupId = null
        }

        val postResult = writePostUseCase(
            accessToken = "Bearer ${accessToken}",
            boardTitle = state.title,
            boardContent = state.content,
            uploadImages = state.uploadImages.map { bitmap ->
                bitmapToByteArray(bitmap)
            },
            tempNickname = "익명의 구운란",
            groupId = groupId,
            hospitalId = hospitalId,
        )

        if (postResult.isSuccess) {
            reduce {
                state.copy(
                    title = "",
                    content = "",
                    uploadImages = listOf(),
                    isLoading = false
                )
            }
            postSideEffect(PostSideEffect.Toast("등록되었습니다"))
            postSideEffect(PostSideEffect.NavigateToListScreen)
        } else {
            postSideEffect(PostSideEffect.Toast("등록에 실패했습니다"))
        }
    }

    fun handleImageSelection(context: Context, uri: Uri) = intent {
        Log.e("WritePostViewModel", "현재 선택한 사진 개수는 ${state.uploadImages.size}개 입니다")
        if (state.uploadImages.size < 4) {
            Log.e("커뮤니티", "이미지 uri $uri")
            val resizedImage = resizeImage(context, uri, 800, 600)
            Log.e("커뮤니티", "$resizedImage")
            resizedImage?.let {
                reduce {
                    state.copy(
                        uploadImages = state.uploadImages + it
                    )
                }
                Log.d("커뮤니티", "handleImageSelection ${state.uploadImages} -  ${state.title}")
            }
        } else {
            postSideEffect(PostSideEffect.Toast("사진은 4개까지 등록할 수 있습니다"))
        }
    }


}

data class WritePostState(
    val title: String = "",
    val content: String = "",
    val uploadImages: List<Bitmap> = listOf(),
    val isLoading: Boolean = false,
)

sealed class PostSideEffect {
    class Toast(val message: String) : PostSideEffect()
    object NavigateToListScreen : PostSideEffect() // 임시로 메인 화면으로 이동
}
