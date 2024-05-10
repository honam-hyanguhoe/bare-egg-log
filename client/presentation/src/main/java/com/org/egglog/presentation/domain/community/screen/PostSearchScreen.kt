package com.org.egglog.presentation.domain.community.screen

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.domain.community.model.PostData
import com.org.egglog.presentation.component.atoms.cards.ResultCard
import com.org.egglog.presentation.component.molecules.headers.SearchHeader
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.domain.community.viewmodel.PostListViewModel
import com.org.egglog.presentation.domain.community.viewmodel.PostSearchViewModel
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.utils.MessageUtil
import com.org.egglog.presentation.utils.heightPercent
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PostSearchScreen(
    hospitalId: Int?,
    groupId: Int?,
    viewModel: PostSearchViewModel = hiltViewModel(),
    onNavigateToPostListScreen: () -> Unit,
    onNavigateToDetailScreen: (postId: Int) -> Unit
) {
    val state = viewModel.collectAsState().value

    PostSearchScreen(
        onNavigateToPostListScreen = onNavigateToPostListScreen,
        searchWord = state.searchWord,
        onChangeSearchWord = viewModel::onSearchChange,
        onClickDone = viewModel::onClickDone,
        searchListFlow = state.searchListFlow,
        onClickPost = { postId: Int -> onNavigateToDetailScreen(postId) }
    )
}

@Composable
private fun PostSearchScreen(
    onNavigateToPostListScreen: () -> Unit,
    searchWord: String?,
    onChangeSearchWord: (String) -> Unit,
    onClickDone: () -> Unit,
    searchListFlow: Flow<PagingData<PostData>>,
    onClickPost: (postId: Int) -> Unit
) {
    val context = LocalContext.current
    val list = searchListFlow.collectAsLazyPagingItems()

    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
            .padding(
                top = 20.heightPercent(context).dp,
            )
    ) {
        SearchHeader(
            onClickBack = onNavigateToPostListScreen,
            placeholder = "검색어를 입력해주세요",
            onClickDone = onClickDone,
            searchWord = searchWord ?: "",
            onChangeSearch = onChangeSearchWord
        )

        LazyColumn(Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
            if (list.itemCount == 0) {
                item {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        ResultCard(message = MessageUtil.NO_SEARCH_RESULT)
                    }
                }
            } else {
                items(
                    count = list.itemCount,
                    key = { index -> list[index]?.boardId ?: "UniqueKey_$index" }
                ) { index ->
                    list[index]?.run {
                        val profile = Profile(
                            this.userId,
                            this.tempNickname ?: "익명의 구운란",
                            this.hospitalName,
                            this.isHospitalAuth
                        )
                        val postInfo = com.org.egglog.client.data.PostInfo(
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
                            PostCard(
                                profile = profile,
                                postInfo = postInfo,
                                postReaction = postReaction,
                                onClick = { postId -> onClickPost(postId) })
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

            }
        }
    }
}