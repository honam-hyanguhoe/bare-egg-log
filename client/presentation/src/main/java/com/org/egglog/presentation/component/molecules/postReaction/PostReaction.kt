package com.org.egglog.presentation.component.molecules.postReaction

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.PostReactionInfo
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.Comment
import com.org.egglog.presentation.utils.FavoriteBorder
import com.org.egglog.presentation.utils.Watch
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.buttons.IconButton
import com.org.egglog.presentation.component.atoms.icons.Icon

@Composable
fun PostReaction(postReactionInfo: PostReactionInfo) {
    val context = LocalContext.current
    Row {
        Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                imageVector = FavoriteBorder,
                size = 14.widthPercent(context).dp,
                color = if(postReactionInfo.isLiked) Error500 else Gray400,
                onClick = {
                    Log.d("좋아요 삽입하기: ", "${postReactionInfo.postId} clicked!!!")
                },
                enabled = postReactionInfo.hitCnt >= 0)
            Spacer(modifier = Modifier.padding(2.widthPercent(context).dp))
            Text(text = "${postReactionInfo.likeCnt}", style = Typography.displayMedium.copy(color = Gray400))
        }
        Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
            Icon(imageVector = Comment, modifier = Modifier.size(14.widthPercent(context).dp), color = if(postReactionInfo.isCommented) Warning300 else Gray400)
            Spacer(modifier = Modifier.padding(2.widthPercent(context).dp))
            Text(text = "${postReactionInfo.commentCnt}", style = Typography.displayMedium.copy(color = Gray400))
        }
        if(postReactionInfo.hitCnt >= 0) {
            Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
                Icon(imageVector = Watch, modifier = Modifier.size(14.widthPercent(context).dp), color = Blue300)
                Spacer(modifier = Modifier.padding(2.widthPercent(context).dp))
                Text(text = "${postReactionInfo.hitCnt}", style = Typography.displayMedium.copy(color = Gray400))
            }
        }
    }
}