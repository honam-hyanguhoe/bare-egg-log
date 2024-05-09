package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.client.data.CommentInfo
import com.org.egglog.client.data.toUiModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.community.usecase.DeleteCommentUseCase
import com.org.egglog.domain.community.usecase.DeletePostUseCase
import com.org.egglog.domain.community.usecase.GetCommentListUseCase
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
    savedStateHandle: SavedStateHandle,
    private val getTokenUseCase: GetTokenUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
): ViewModel(), ContainerHost<PostDetailState, PostDetailSideEffect> {

    private val postId: Int = savedStateHandle.get<Int>("postId") ?: throw IllegalArgumentException("postId is required")
    private var accessToken: String ?= "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwicm9sZSI6IkdFTkVSQUxfVVNFUiIsImlhdCI6MTcxNTIyOTYxOCwiZXhwIjoxNzE2MDkzNjE4fQ.fWGMfbiuC5OPETj3Fiprhw4jQNX-Rt1KD66rKXXkwbs"

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
//         accessToken = getTokenUseCase().first!!

        val userDetail = getUserUseCase("Bearer ${accessToken}").getOrThrow()
        val userId = userDetail!!.id

        val postDetailInfo = getPostDetailUseCase("Bearer ${accessToken}", postId).map {
            it!!.toUiModel()
        }.getOrThrow()

        val commentList = getCommentListUseCase("Bearer ${accessToken}", postId).map {
            it?.map {
               it.toUiModel()
            }
        }.getOrThrow()
        Log.e("PostDetailViewModel", "commentList는 ${commentList}")

        reduce {
            state.copy(
                postDetailInfo = postDetailInfo,
                userId = userId,
                commentList = commentList
            )
        }
    }

    fun onDeletePost() = intent {

        if(deletePostUseCase("Bearer ${accessToken}", postId).isSuccess) {
            postSideEffect(PostDetailSideEffect.Toast("삭제되었습니다"))
            postSideEffect(PostDetailSideEffect.NavigateToCommunityActivity)
        } else {
            postSideEffect(PostDetailSideEffect.Toast("삭제 실패"))
        }
    }

    fun onDeleteComment(commentId: Long) = intent {
        val result = deleteCommentUseCase("Bearer ${accessToken}", commentId)

        if (result.isSuccess) {
            // 댓글 삭제 후 댓글 리스트 다시 조회
            val newCommentList = getCommentListUseCase("Bearer ${accessToken}", postId).map {
                it?.map {
                    it.toUiModel()
                }
            }.getOrThrow()
            reduce {
                state.copy (
                    commentList = newCommentList
                )
            }
            postSideEffect(PostDetailSideEffect.Toast("댓글이 삭제되었습니다"))
        } else {
            postSideEffect(PostDetailSideEffect.Toast("댓글 삭제에 실패했습니다"))
        }

    }

}


data class PostDetailState(
    val postDetailInfo: PostDetailInfo ?= null,
    val userId: Long ?= -1,
    val commentList: List<CommentInfo> ?= emptyList()
)

sealed interface PostDetailSideEffect {
    class Toast(val message: String) : PostDetailSideEffect
    object NavigateToCommunityActivity: PostDetailSideEffect
}


