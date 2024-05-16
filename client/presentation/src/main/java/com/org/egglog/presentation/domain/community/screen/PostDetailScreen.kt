package com.org.egglog.presentation.domain.community.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.org.egglog.client.data.CommentInfo
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.cards.CommentCard
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.data.PostDetailInfo
import com.org.egglog.presentation.domain.community.activity.CommunityActivity
import com.org.egglog.presentation.domain.community.viewmodel.PostDetailSideEffect
import com.org.egglog.presentation.domain.community.viewmodel.PostDetailViewModel
import com.org.egglog.presentation.domain.community.viewmodel.PostListSideEffect
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.Gray200
import com.org.egglog.presentation.theme.Gray400
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Warning200
import com.org.egglog.presentation.utils.Send
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.org.egglog.presentation.theme.NaturalBlack

@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel = hiltViewModel(),
    postId: Int,
    onNavigateToPostListScreen: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PostDetailSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()

            PostDetailSideEffect.NavigateToCommunityActivity -> {
                context.startActivity(
                    Intent(
                        context, CommunityActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    PostDetailScreen(
        userId = state.userId!!,
        postId = postId,
        parentId = state.parentId,
        postDetailInfo = state.postDetailInfo,
        commentList = state.commentList,
        commentValue = state.comment,
        onNavigateToPostListScreen = onNavigateToPostListScreen,
        onDeletePost = viewModel::onDeletePost,
        onClickModify = {}, // 수정 버튼 클릭시
        onDeleteComment = viewModel::onDeleteComment,
        onChangeComment = viewModel::onChangeComment,
        onClickRecomment = viewModel::onClickRecomment,
        onClickSend = viewModel::onClickSend,
        onClickLike = viewModel::onClickLike,
        swipeRefreshState = swipeRefreshState,
        refreshSomething = viewModel::refreshSomething
    )
}


@Composable
private fun PostDetailScreen(
    userId: Long,
    postId: Int,
    parentId: Long,
    postDetailInfo: PostDetailInfo?,
    commentList: List<CommentInfo>?,
    commentValue: String,
    onNavigateToPostListScreen: () -> Unit,
    onDeletePost: () -> Unit,
    onClickModify: () -> Unit,
    onDeleteComment: (Long) -> Unit,
    onChangeComment: (String) -> Unit,
    onClickRecomment: (Long) -> Unit,
    onClickSend: () -> Unit,
    onClickLike: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
    refreshSomething: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalWhite),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var selectedMenuItem by remember { mutableStateOf<String?>(null) }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current


        Column(modifier = Modifier.weight(1f)) {
            // 내 글일 경우만 수정, 삭제 버튼 활성화
            if (userId.toInt() == postDetailInfo?.userId) {
                Log.e("PostDetail", "${userId.toInt()}, ${postDetailInfo.userId}, ${postId}")
                BasicHeader(
                    hasArrow = true,
                    hasMore = true,
                    onClickBack = onNavigateToPostListScreen,
//                    options = listOf("수정하기", "삭제하기"),
                    options = listOf("삭제하기"),
                    selectedOption = selectedMenuItem,
                    onSelect = {
                        selectedMenuItem = it
                        if (it == "삭제하기") {
                            onDeletePost()
                        } else if (it == "수정하기") {
                            // TODO : 게시글 수정 로직 추가
                            onClickModify()
                        }
                    }

                )
            } else {
                BasicHeader(
                    hasArrow = true,
                    onClickBack = onNavigateToPostListScreen,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = refreshSomething,
                indicator = { state, refreshTrigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshTrigger,
                        backgroundColor = NaturalBlack,
                        contentColor = NaturalWhite
                    )
                }
            ) {
                LazyColumn(Modifier.padding(bottom = 10.dp)) {
                    if (postDetailInfo != null) {
                        item {
                            val profile = Profile(
                                postDetailInfo.userId.toLong(),
                                postDetailInfo.tempNickname ?: "익명의 구운란",
                                postDetailInfo.hospitalName ?: "",
                                postDetailInfo.isHospitalAuth
                            )
                            val postInfo = com.org.egglog.client.data.PostInfo(
                                postDetailInfo.boardId,
                                postDetailInfo.boardTitle,
                                postDetailInfo.boardContent,
                                postDetailInfo.boardCreatedAt,
                                postDetailInfo.pictureOne,
                                postDetailInfo.pictureTwo ?: "",
                                postDetailInfo.pictureThree ?: "",
                                postDetailInfo.pictureFour ?: ""
                            )
                            val postReaction = PostReactionInfo(
                                postDetailInfo.boardId,
                                postDetailInfo.boardLikeCount,
                                postDetailInfo.commentCount,
                                postDetailInfo.viewCount,
                                postDetailInfo.isLiked,
                                postDetailInfo.isCommented
                            )
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                PostCard(
                                    profile = profile,
                                    postInfo = postInfo,
                                    postReaction = postReaction,
                                    onClickLike = onClickLike
                                )
                            }
                        }
                    } else {
                        item {
                            Text(text = "게시글을 불러오는데 실패했습니다.")
                        }
                    }

                    if (commentList!!.isNotEmpty()) {
                        items(
                            count = commentList.size,
                            key = { index -> commentList[index].commentId }
                        ) { index ->
                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                val commentInfo = commentList[index]
                                // 게시글 작성자와 댓글 작성자의 userId 비교
                                val isPostAuthor = commentInfo.userId == userId
                                // 댓글 작성자가 게시글 작성자와 같을 경우 tempNickname을 변경
                                val tempNickname =
                                    if (isPostAuthor) "익명의 구운란" else commentInfo.tempNickname

                                CommentCard(
                                    commentInfo = commentInfo.copy(tempNickname = tempNickname),
                                    myUserId = userId,
                                    onDeleteClick = { commentId ->
                                        onDeleteComment(commentId)
                                    },
                                    onRecommentClick = { commentId ->
                                        focusManager.moveFocus(FocusDirection.Previous)
                                        onClickRecomment(commentId)
                                    }
                                )

                                if (commentInfo.recomment.isNotEmpty()) {
                                    commentInfo.recomment.forEach { recomment ->
                                        val recommentTempNickname =
                                            if (isPostAuthor) "익명의 구운란" else recomment.tempNickname

                                        CommentCard(
                                            true,
                                            commentInfo = recomment.copy(tempNickname = recommentTempNickname),
                                            myUserId = userId,
                                            onDeleteClick = { commentId ->
                                                onDeleteComment(commentId)
                                            },
                                            onRecommentClick = {}
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SingleInput(
                modifier = Modifier.fillMaxWidth(0.8f),
                text = commentValue,
                placeholder = if (parentId.toInt() == 0) "댓글을 입력해주세요" else "대댓글을 입력해주세요",
                onValueChange = { commentValue -> onChangeComment(commentValue) },
                focusManager = focusManager,
            )
            Box(
                modifier = Modifier
                    .background(
                        if (commentValue == "") Gray200 else Warning200,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            ) {
                CustomIconButton(
                    size = 36.dp,
                    imageVector = Send,
                    color = NaturalWhite,
                    onClick = {
                        onClickSend()
                        focusManager.clearFocus()
                    },
                    enabled = commentValue != ""
                )
            }
        }
    }
}