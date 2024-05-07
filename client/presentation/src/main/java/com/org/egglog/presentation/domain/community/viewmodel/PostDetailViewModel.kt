package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.community.usecase.GetPostDetailUseCase
import com.org.egglog.presentation.data.PostDetailInfo
import com.org.egglog.presentation.data.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTokenUseCase: GetTokenUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel(), ContainerHost<PostDetailState, PostDetailSideEffect> {

    private val postId: Int = savedStateHandle.get<Int>("postId") ?: throw IllegalArgumentException("postId is required")

    override val container: Container<PostDetailState, PostDetailSideEffect> = container(
        initialState = PostDetailState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {postSideEffect(PostDetailSideEffect.Toast(throwable.message.orEmpty()))}
            }
        }
    )

    init {
        load()
    }

    private fun load() = intent {
        Log.e("PostDetailViewModel", "postId는 ${postId}")
        val tokens = getTokenUseCase()

        val userDetail = getUserUseCase("Bearer ${tokens.first}").getOrThrow()
        val userId = userDetail!!.id


        Log.e("PostDetailViewModel", "PostDetailInfo는 ${getPostDetailUseCase("Bearer ${tokens.first}", postId)}")
        val postDetailInfo = getPostDetailUseCase("Bearer ${tokens.first}", postId).map {
            it!!.toUiModel()
        }.getOrThrow()

        Log.e("PostDetailViewModel", "PostDetailInfo는 ${postDetailInfo}")
        reduce {
            state.copy(
                postDetailInfo = postDetailInfo,
                userId = userId
            )
        }
    }

}


data class PostDetailState(
    val postDetailInfo: PostDetailInfo ?= null,
    val userId: Long ?= -1
)

sealed interface PostDetailSideEffect {
    class Toast(val message: String) : PostDetailSideEffect
}


