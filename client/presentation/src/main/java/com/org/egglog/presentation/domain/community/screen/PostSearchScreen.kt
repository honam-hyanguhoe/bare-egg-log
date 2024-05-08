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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.component.atoms.cards.ResultCard
import com.org.egglog.presentation.component.molecules.headers.SearchHeader
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.data.PreviewPostInfo
import com.org.egglog.presentation.domain.community.viewmodel.PostListViewModel
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.utils.MessageUtil
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PostSearchScreen(
    viewModel: PostListViewModel = hiltViewModel(),
    onNavigateToPostListScreen: () -> Unit
) {
    val state = viewModel.collectAsState().value

    PostSearchScreen(
        onNavigateToPostListScreen = onNavigateToPostListScreen,
        searchWord = state.searchWord,
        onChangeSearchWord = viewModel::onSearchChange,
        onClickDone = viewModel::onClickDone,
        searchList = state.searchList,
        onClickPost = {}
    )
}

@Composable
private fun PostSearchScreen (
    onNavigateToPostListScreen: () -> Unit,
    searchWord: String?,
    onChangeSearchWord: (String) -> Unit,
    onClickDone: () -> Unit,
    searchList: List<PreviewPostInfo>,
    onClickPost: () -> Unit
) {
    val context = LocalContext.current
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
            if(searchList.isEmpty()) {
                item {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        ResultCard(message = MessageUtil.NO_SEARCH_RESULT)
                    }

                }
            } else {
                items(
                    count = searchList.size,
                    key = {index -> searchList[index].boardId}
                ) {
                        index ->
                    val postData = searchList[index]
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
                    )
                }
            }
        }

    }
}