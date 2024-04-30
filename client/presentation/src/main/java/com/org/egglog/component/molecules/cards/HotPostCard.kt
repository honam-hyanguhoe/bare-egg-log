package com.org.egglog.component.molecules.cards

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
import com.org.egglog.utils.CommentBorder
import com.org.egglog.utils.Favorite
import com.org.egglog.utils.FavoriteBorder
import com.org.egglog.utils.widthPercent
import com.org.egglog.component.atoms.cards.BackgroundCard
import com.org.egglog.component.atoms.icons.Icon
import com.org.egglog.theme.*

class PostInfo(val title: String,val name: String, val likeCnt: Int, val commentCnt: Int, val isLiked: Boolean)

@Composable
fun HotPostCard(postInfo: PostInfo, onClickPost: () -> Unit) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = if(postInfo.isLiked) Favorite else FavoriteBorder, Modifier.size(12.dp), color = Gray500)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${postInfo.likeCnt}",color = Gray500, style = Typography.labelSmall)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = CommentBorder, Modifier.size(12.dp), color = Gray500)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${postInfo.commentCnt}",color = Gray500, style = Typography.labelSmall)
                }
            }
        }
    }
}