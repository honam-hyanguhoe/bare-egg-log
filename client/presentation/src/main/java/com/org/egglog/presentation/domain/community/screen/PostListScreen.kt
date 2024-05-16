package com.org.egglog.presentation.domain.community.screen

import android.content.Intent
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.org.egglog.client.data.PostInfo
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.cards.BackgroundCard
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.molecules.bottomNavigator.BottomNavigator
import com.org.egglog.presentation.component.molecules.cards.HotPostCard
import com.org.egglog.presentation.component.molecules.headers.NoticeHeader
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.data.HotPostInfo
import com.org.egglog.presentation.domain.community.viewmodel.PostListSideEffect
import com.org.egglog.presentation.domain.community.viewmodel.PostListViewModel
import com.org.egglog.presentation.domain.group.activity.GroupActivity
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.domain.myCalendar.activity.MyCalendarActivity
import com.org.egglog.presentation.domain.setting.activity.SettingActivity
import com.org.egglog.presentation.theme.Error200
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning200
import com.org.egglog.presentation.utils.Edit
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PostListScreen(
    viewModel: PostListViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (postId: Int) -> Unit,
    onNavigateToSearchScreen: (hospitalId: Int?, groupId: Int?) -> Unit,
    onNavigateToWriteScreen: (hospitalId: Int?, groupId: Int?) -> Unit
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PostListSideEffect.Toast -> Toast.makeText(
                context, sideEffect.message, Toast.LENGTH_SHORT
            ).show()

            PostListSideEffect.NavigateToGroupActivity -> {
                context.startActivity(
                    Intent(
                        context, GroupActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            PostListSideEffect.NavigateToMainActivity -> {
                context.startActivity(
                    Intent(
                        context, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            PostListSideEffect.NavigateToSettingActivity -> {
                context.startActivity(
                    Intent(
                        context, SettingActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            PostListSideEffect.NavigateToMyCalendarActivity -> {
                context.startActivity(
                    Intent(
                        context, MyCalendarActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    PostListScreen(
        selectedIdx = state.selectedIdx,
        onSelectedIdx = viewModel::onSelectedIdx,
        isHospitalAuth = state.isHospitalAuth,
        categoryName = state.categoryName,
        hospitalId = state.hospitalId,
        groupId = state.groupId,
        categoryList = state.categoryList,
        postListFlow = state.postListFlow,
        hotPostList = state.hotPostList,
        onClickPost = { postId: Int -> onNavigateToDetailScreen(postId) },
        onClickWriteButton = onNavigateToWriteScreen,
        onSelectCategory = viewModel::onSelectCategoryIndex,
        onClickSearch = onNavigateToSearchScreen,
        swipeRefreshState = swipeRefreshState,
        refreshSomething = viewModel::refreshSomething,
    )
}

@Composable
private fun PostListScreen(
    selectedIdx: Int,
    onSelectedIdx: (Int) -> Unit,
    isHospitalAuth: Boolean,
    categoryName: String,
    hospitalId: Int?,
    groupId: Int?,
    categoryList: List<Pair<Int, String>>,
    postListFlow: Flow<PagingData<PostData>>,
    hotPostList: List<HotPostInfo>,
    onClickPost: (postId: Int) -> Unit ,
    onClickWriteButton: (hospitalId: Int?, groupId: Int?) -> Unit,
    onSelectCategory: (index: Int) -> Unit,
    onClickSearch: (hospitalId: Int?, groupId: Int?) -> Unit,
    swipeRefreshState: SwipeRefreshState,
    refreshSomething: () -> Unit
) {
    val context = LocalContext.current
    var selectedMenuItem by remember { mutableStateOf(categoryList[0].second) }
    val list = postListFlow.collectAsLazyPagingItems()
    val fabInteractionSource = remember { MutableInteractionSource() }
    var scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val refreshingState = remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
    ) {
        Column(
            Modifier
                .fillMaxHeight(0.9f)
                .padding(
                    start = 10.widthPercent(context).dp,
                    top = 20.heightPercent(context).dp,
                    end = 10.widthPercent(context).dp
                )
        ) {
            NoticeHeader(hasSearch = true,
                hasMenu = true,
                title = categoryName,
                selectedOption = selectedMenuItem,
                options = categoryList.map { it.second },
                onClickSearch = { onClickSearch(hospitalId ?: -1, groupId ?: -1) },
                onClickNotification = {},
                onSelect = {
                    selectedMenuItem = it
                    val selectedPair = categoryList.find { it.second == selectedMenuItem }
                    val index = categoryList.indexOf(selectedPair)
                    onSelectCategory(index)
                })

            Spacer(modifier = Modifier.height(10.dp))

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
                LazyColumn(state = scrollState, modifier = Modifier.padding(10.dp)) {
                    item {
                        BackgroundCard(
                            margin = 0.dp,
                            padding = 0.dp,
                            color = Warning200,
                            borderRadius = 10.dp,
                            onClickCard = null
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.padding(vertical = 14.dp, horizontal = 4.dp)) {
                                    Text(text = "에그로그에 친구를 초대하고,", style = Typography.displayLarge)
                                    Text(
                                        text = "친구와 함께 일정을 공유하세요!",
                                        style = Typography.displayLarge
                                    )
                                }

                                Column(
                                    Modifier
                                        .fillMaxHeight()
                                        .height(70.heightPercent(context).dp)
                                        .padding(top = 10.dp, end = 14.dp),
                                    verticalArrangement = Arrangement.Bottom,
                                    horizontalAlignment = Alignment.End
                                ) {
                                    LocalImageLoader(
                                        imageUrl = R.drawable.dark,
                                        Modifier.size(60.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (groupId == null && hospitalId == null) {
                        item {
                            Column {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "\uD83D\uDD25 급상승 게시글")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        if (hotPostList.isNotEmpty()) {
                            items(count = hotPostList.size,
                                key = { index -> "0${hotPostList[index].postId}" }) { index ->
                                val tempNickname = hotPostList[index].name ?: "익명의 구운란"
                                HotPostCard(postInfo = hotPostList[index].copy(name = tempNickname),
                                    onClickPost = { postId -> onClickPost(postId) })
                            }
                        } else {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp, bottom = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "아직 급상승 게시글이 없어요")
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    if (list.itemCount != 0) {
                        items(count = list.itemCount,
                            key = { index ->
                                list[index]?.boardId ?: "UniqueKey_$index"
                            }) { index ->
                            list[index]?.run {
                                val profile = Profile(
                                    this.userId,
                                    this.tempNickname ?: "익명의 구운란",
                                    this.hospitalName,
                                    this.isHospitalAuth
                                )
                                val postInfo = PostInfo(
                                    this.boardId,
                                    this.boardTitle,
                                    this.boardContent,
                                    this.boardCreatedAt,
                                    this.pictureOne
                                )
                                val postReaction = PostReactionInfo(
                                    this.boardId,
                                    this.likeCount,
                                    this.commentCount,
                                    this.viewCount,
                                    this.isLiked,
                                    this.isCommented
                                )

                                Column {
                                    PostCard(profile = profile,
                                        postInfo = postInfo,
                                        postReaction = postReaction,
                                        onClick = { postId -> onClickPost(postId) })
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                                    .padding(top = 20.dp, bottom = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "아직 등록된 게시글이 없어요")
                            }
                        }
                    }
                }
            }

        }
        BottomNavigator(selectedItem = selectedIdx, onItemSelected = { onSelectedIdx(it) })

    }

    // Floating Action Button 영역
    CustomFloatingActionButton(
        hospitalId = hospitalId,
        groupId = groupId,
        isHospitalAuth = isHospitalAuth,
        onClickWriteButton
    )
}

@Composable
fun CustomFloatingActionButton(
    hospitalId: Int?,
    groupId: Int?,
    isHospitalAuth: Boolean,
    onClickWriteButton: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val fabInteractionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(top = 0.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                if (hospitalId != null && !isHospitalAuth) {
                    // 사용자가 병원 인증 된 유저가 아니면 인증해달라는 toast
                    Toast.makeText(context, "병원 게시판은 인증된 사용자만 작성할 수 있습니다", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    onClickWriteButton(hospitalId ?: -1, groupId ?: -1)
                }
            },
            modifier = Modifier
                .padding(5.dp),
            shape = FloatingActionButtonDefaults.largeShape,
            containerColor = NaturalBlack,
            contentColor = NaturalWhite,
            elevation = FloatingActionButtonDefaults.elevation(),
            interactionSource = fabInteractionSource
        ) {
            Icon(imageVector = Edit, contentDescription = "fab Icon")
        }
    }
}