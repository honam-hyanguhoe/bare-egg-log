package com.org.egglog.presentation.domain.community.screen

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
import androidx.wear.compose.material.Text
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.postCard.PostCard
import com.org.egglog.presentation.data.PreviewPostInfo
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun PostDetailScreen(
    onNavigateToPostListScreen: () -> Unit
) {
    val context = LocalContext.current
    val postData = PreviewPostInfo(31, "test1", "test1", "2024-01-02 17:39:38", "익명의 구운란", 1, 5, 0, "https://picsum.photos/300", false, true, true, 1, "전남대병원")
    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
    ) {
        BasicHeader(hasArrow = true, hasMore = true, onClickBack = onNavigateToPostListScreen, options = listOf("수정하기", "삭제하기"))


        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            item {
                val profile = Profile(postData.userId, postData.tempNickname, postData.hospitalName, postData.isHospitalAuth)
                val postInfo = com.org.egglog.client.data.PostInfo(postData.boardId, postData.boardTitle, postData.boardContent, postData.pictureOne)
                val postReaction = PostReactionInfo(postData.boardId, postData.likeCount, postData.commentCount, postData.viewCount, postData.isLiked, postData.isCommented)
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    PostCard(profile = profile, postInfo = postInfo, postReaction = postReaction)
                }
            }

            item {

            }
        }


    }
}