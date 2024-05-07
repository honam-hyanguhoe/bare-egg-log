package com.org.egglog.presentation.component.molecules.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.client.data.PostInfo
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.presentation.utils.CommentBorder
import com.org.egglog.presentation.utils.Favorite
import com.org.egglog.presentation.utils.FavoriteBorder
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.cards.BackgroundCard
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.molecules.postReaction.PostReaction
import com.org.egglog.presentation.data.HotPostInfo
import com.org.egglog.presentation.theme.*

@Composable
fun HotPostCard(postInfo: HotPostInfo, onClickPost: () -> Unit) {
    val context = LocalContext.current

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 16.widthPercent(context).dp, color = Gray100, borderRadius = 10.widthPercent(context).dp, onClickCard = onClickPost) {
        Column(Modifier.fillMaxWidth()) {
            Text(text = "${postInfo.title}", color = NaturalBlack, style = Typography.displayLarge.copy(fontSize = 16.sp))
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                Text("${postInfo.name}", color= Gray500, style = Typography.labelSmall.copy(fontSize = 14.sp))
                PostReaction(postReactionInfo = PostReactionInfo(1, likeCnt = postInfo.likeCnt, commentCnt = postInfo.commentCnt, isLiked = postInfo.isLiked, isCommented = false, ))
            }
        }
    }
}