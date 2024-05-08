package com.org.egglog.presentation.domain.community.screen

import android.app.ProgressDialog.show
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.annotations.concurrent.Background
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.FloatingButton
import com.org.egglog.presentation.component.atoms.cards.BackgroundCard
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.molecules.cards.HotPostCard
import com.org.egglog.presentation.component.molecules.headers.NoticeHeader
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.data.HotPostInfo
import com.org.egglog.presentation.data.PreviewPostInfo
import com.org.egglog.presentation.domain.community.posteditor.activity.PostEditorActivity
import com.org.egglog.presentation.domain.community.viewmodel.PostListSideEffect
import com.org.egglog.presentation.domain.community.viewmodel.PostListViewModel
import com.org.egglog.presentation.theme.BlueGray900
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning200
import com.org.egglog.presentation.utils.Close
import com.org.egglog.presentation.utils.Edit
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PostListScreen(
    viewModel: PostListViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (postId: Int) -> Unit
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect -> when(sideEffect) {
        is PostListSideEffect.Toast -> Toast.makeText(context , sideEffect.message, Toast.LENGTH_SHORT).show()
        PostListSideEffect.NavigateToWriteScreen -> (context.startActivity(Intent(context, PostEditorActivity::class.java)))
    }
    }
    PostListScreen(
        postList = state.postList,
        hotPostList = state.hotPostList,
        onClickPost = { postId: Int -> onNavigateToDetailScreen(postId) },
        onClickWriteButton = viewModel::onClickWriteButton
    )
}

@Composable
private fun PostListScreen(
    postList: List<PreviewPostInfo>,
    hotPostList: List<HotPostInfo>,
    onClickPost: (postId: Int) -> Unit,
    onClickWriteButton: () -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
            .padding(
                start = 10.widthPercent(context).dp,
                top = 20.heightPercent(context).dp,
                end = 10.widthPercent(context).dp
            )
    ) {
        NoticeHeader(
            hasSearch = true,
            title = "커뮤니티",
            selectedOption = "",
            onClickSearch = {},
            onClickNotification = {},
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(Modifier.padding(10.dp)) {
            item {
                BackgroundCard(margin=0.dp, padding=0.dp, color= Warning200, borderRadius=10.dp, onClickCard = null) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.padding(vertical = 14.dp, horizontal = 4.dp)){
                            Text(text = "에그로그에 친구를 초대하고,", style= Typography.displayLarge)
                            Text(text = "친구와 함께 일정을 공유하세요!", style= Typography.displayLarge)
                        }


                        Column(
                            Modifier
                                .fillMaxHeight()
                                .height(70.heightPercent(context).dp)
                                .padding(top = 10.dp, end = 14.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.End) {
                            LocalImageLoader(imageUrl = R.drawable.dark, Modifier.size(60.dp))
                        }
                    }
                }
            }

            item {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "\uD83D\uDD25 급상승 게시글")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if(hotPostList.isNotEmpty()) {
                items(
                    count = hotPostList.size,
                    key = { index -> "0${hotPostList[index].postId}" }
                ) { index ->
                    HotPostCard(postInfo = hotPostList[index], onClickPost = { postId -> onClickPost(postId) })
                }
            } else {
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "아직 급상승 게시글이 없어요")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }

            if(postList.isNotEmpty()) {
                items(
                    count = postList.size,
                    key = { index -> postList[index].boardId }
                ) { index ->
                    val postData = postList[index]
                    val profile = Profile(
                        postData.userId,
                        postData.tempNickname ?: "익명의 구운란",
                        postData.hospitalName,
                        postData.isHospitalAuth
                    )
                    val postInfo = com.org.egglog.client.data.PostInfo(
                        postData.boardId,
                        postData.boardTitle,
                        postData.boardContent,
                        postData.pictureOne
                    )
                    val postReaction = PostReactionInfo(
                        postData.boardId,
                        postData.likeCount,
                        postData.commentCount,
                        postData.viewCount,
                        postData.isLiked,
                        postData.isCommented
                    )

                    PostCard(
                        profile = profile,
                        postInfo = postInfo,
                        postReaction = postReaction,
                        onClick = { postId -> onClickPost(postId) })
                }
            } else {
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "아직 등록된 게시글이 없어요")
                    }
                }
            }
        }

    }

    val fabInteractionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End) {
        FloatingActionButton(
            onClick = onClickWriteButton,
            modifier = Modifier
                .padding(5.dp),
            shape = FloatingActionButtonDefaults.largeShape,
            containerColor = BlueGray900,
            contentColor = NaturalWhite,
            elevation = FloatingActionButtonDefaults.elevation(),
            interactionSource = fabInteractionSource) {
            Icon(imageVector = Edit, contentDescription = "fab Icon")
        }
    }
}