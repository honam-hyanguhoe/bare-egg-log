package com.org.egglog.presentation.domain.community.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.client.data.CommentInfo
import com.org.egglog.client.data.toUiModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserUseCase
import com.org.egglog.domain.community.usecase.CreateCommentUseCase
import com.org.egglog.domain.community.usecase.CreateLikeUseCase
import com.org.egglog.domain.community.usecase.DeleteCommentUseCase
import com.org.egglog.domain.community.usecase.DeleteLikeUseCase
import com.org.egglog.domain.community.usecase.DeletePostUseCase
import com.org.egglog.domain.community.usecase.GetCommentListUseCase
import com.org.egglog.domain.community.usecase.GetPostDetailUseCase
import com.org.egglog.presentation.data.PostDetailInfo
import com.org.egglog.presentation.data.toUiModel
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
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTokenUseCase: GetTokenUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val createLikeUseCase: CreateLikeUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase
): ViewModel(), ContainerHost<PostDetailState, PostDetailSideEffect> {

    private val postId: Int = savedStateHandle.get<Int>("postId") ?: throw IllegalArgumentException("postId is required")
    private var accessToken: String ?= ""

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

    // 초기 렌더링
    private fun load() = intent {
         accessToken = getTokenUseCase().first!!

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

        reduce {
            state.copy(
                postDetailInfo = postDetailInfo,
                userId = userId,
                commentList = commentList
            )
        }
    }

    // 게시글 삭제 시
    fun onDeletePost() = intent {

        if(deletePostUseCase("Bearer ${accessToken}", postId).isSuccess) {
            postSideEffect(PostDetailSideEffect.Toast("삭제되었습니다"))
            postSideEffect(PostDetailSideEffect.NavigateToCommunityActivity)
        } else {
            postSideEffect(PostDetailSideEffect.Toast("삭제 실패"))
        }
    }

    // 댓글 삭제 시
    fun onDeleteComment(commentId: Long) = intent {
        val result = deleteCommentUseCase("Bearer ${accessToken}", commentId)

        if (result.isSuccess) {
            // 댓글 삭제 후 댓글 리스트 다시 조회
            val newCommentList = getCommentListUseCase("Bearer ${accessToken}", postId).map {
                it?.map {
                    it.toUiModel()
                }
            }.getOrThrow()
            val commentInfo = state.commentList!!.find { it.commentId == commentId }
            val commentSize = commentInfo?.recomment?.size?.plus(1)?: 1
            reduce {
                state.copy (
                    commentList = newCommentList,
                    postDetailInfo = state.postDetailInfo!!.copy(commentCount = state.postDetailInfo!!.commentCount - commentSize)
                )
            }
            postSideEffect(PostDetailSideEffect.Toast("댓글이 삭제되었습니다"))
        } else {
            postSideEffect(PostDetailSideEffect.Toast("댓글 삭제에 실패했습니다"))
        }
    }

    // 댓글 내용 작성시
    @OptIn(OrbitExperimental::class)
    fun onChangeComment(value: String) = blockingIntent {
        reduce {
            state.copy(
                comment = value
            )
        }
    }

    // 댓글 작성 버튼 클릭시
    fun onClickSend() = intent {
        val tempNickname: String = if(postId==state.postDetailInfo?.userId) "익명의 구운란" else "익명의 완숙란"

        val result = createCommentUseCase("Bearer ${accessToken}", postId, state.comment, state.parentId, tempNickname)
        if(result.isSuccess) {
            val newCommentList = getCommentListUseCase("Bearer ${accessToken}", postId).map {
                it?.map {
                    it.toUiModel()
                }
            }.getOrThrow()
            reduce {
                state.copy (
                    commentList = newCommentList,
                    postDetailInfo = state.postDetailInfo!!.copy (commentCount = state.postDetailInfo!!.commentCount + 1)
                )
            }
            postSideEffect(PostDetailSideEffect.Toast("댓글이 등록되었습니다"))
        } else {
            postSideEffect(PostDetailSideEffect.Toast("댓글 등록에 실패했습니다"))
        }
        reduce {
            state.copy (
                parentId = 0,
                comment = ""
            )
        }

    }

    // 답댓달기 클릭시
    fun onClickRecomment(parentId: Long) = intent {
        reduce {
            state.copy (
                parentId = parentId
            )
        }
    }

    // 좋아요 클릭시
    fun onClickLike() = intent {
        if (state.postDetailInfo?.isLiked == true) {
            // 좋아요가 눌러져 있다면, 좋아요 삭제
            val response: Result<Boolean> = deleteLikeUseCase("Bearer ${accessToken}", postId)
            // 요청이 성공 했다면 state의 isLiked 값 false로 변경
            if(response.isSuccess) {
                reduce {
                    state.copy (
                        postDetailInfo = state.postDetailInfo!!.copy(isLiked = false, boardLikeCount = state.postDetailInfo!!.boardLikeCount - 1)
                    )
                }
            } else {
                postSideEffect(PostDetailSideEffect.Toast("좋아요 처리 실패"))
            }

        } else {
            // 좋아요가 눌러져 있지 않다면, 좋아요 삽입
            val response: Result<Boolean> = createLikeUseCase("Bearer ${accessToken}", postId)
            // 요청이 성공 했다면 state의 isLiked 값 true로 변경
            if(response.isSuccess) {
                reduce {
                    state.copy (
                        postDetailInfo = state.postDetailInfo!!.copy(isLiked = true, boardLikeCount = state.postDetailInfo!!.boardLikeCount + 1)
                    )
                }
            } else {
                postSideEffect(PostDetailSideEffect.Toast("좋아요 처리 실패"))
            }
        }
    }
}


data class PostDetailState(
    val postDetailInfo: PostDetailInfo ?= null,
    val userId: Long ?= -1,
    val commentList: List<CommentInfo> ?= emptyList(),
    val comment: String = "",
    val parentId: Long = 0
)

sealed interface PostDetailSideEffect {
    class Toast(val message: String) : PostDetailSideEffect
    object NavigateToCommunityActivity: PostDetailSideEffect
}

