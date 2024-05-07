package com.org.egglog.presentation.domain.community.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.data.PostDetailInfo
import com.org.egglog.presentation.data.PreviewPostInfo
import com.org.egglog.presentation.domain.community.viewmodel.PostDetailSideEffect
import com.org.egglog.presentation.domain.community.viewmodel.PostDetailViewModel
import com.org.egglog.presentation.domain.community.viewmodel.PostListSideEffect
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel = hiltViewModel(),
    postId: Int,
    onNavigateToPostListScreen: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value
    viewModel.collectSideEffect {
        sideEffect -> when(sideEffect) {
        is PostDetailSideEffect.Toast -> Toast.makeText(context , sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    PostDetailScreen(state.userId!!, postId, state.postDetailInfo, onNavigateToPostListScreen)
}

@Composable
private fun PostDetailScreen(
    userId: Long,
    postId: Int,
    postDetailInfo: PostDetailInfo?,
    onNavigateToPostListScreen: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
    ) {

        // 내 글일 경우만 수정, 삭제 버튼 활성화
        if(userId.toInt() == postDetailInfo?.userId) {
            BasicHeader(
                hasArrow = true,
                hasMore = true,
                onClickBack = onNavigateToPostListScreen,
                options = listOf("수정하기", "삭제하기")
            )
        } else {
            BasicHeader(
                hasArrow = true,
                onClickBack = onNavigateToPostListScreen,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            if(postDetailInfo!=null) {
                item {
                    val profile = Profile(
                        postDetailInfo.userId.toLong(),
                        postDetailInfo.tempNickname ?: "익명의 구운란",
                        postDetailInfo.hospitalName?: "",
                        postDetailInfo.isHospitalAuth
                    )
                    val postInfo = com.org.egglog.client.data.PostInfo(
                        postDetailInfo.boardId,
                        postDetailInfo.boardTitle,
                        postDetailInfo.boardContent,
                        postDetailInfo.pictureOne
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
                            postReaction = postReaction
                        )
                    }
                }
            } else {
                item {
                    Text(text = "게시글을 불러오는데 실패했습니다.")
                }
            }

            item {
                // 댓글 리스트
            }
        }


    }
}