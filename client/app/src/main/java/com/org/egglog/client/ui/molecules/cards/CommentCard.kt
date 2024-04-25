package com.org.egglog.client.ui.molecules.cards

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.CommentInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.profileItem.ProfileItem
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.ArrowRecommend
import com.org.egglog.client.utils.Trash
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent

@Composable
fun CommentCard(commentInfo: CommentInfo, myUserId: Long) {
    val context = LocalContext.current
    Row(
        Modifier
            .background(color = Gray100)
            .padding(8.widthPercent(context).dp, 14.heightPercent(context).dp), Arrangement.Start, Alignment.CenterVertically) {
        if(commentInfo.recomment.size == 0) {
            Icon(imageVector = ArrowRecommend, modifier = Modifier.size(18.widthPercent(context).dp), color = Gray600)
            Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
        }
        Column {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                ProfileItem(
                    profile = Profile(
                        commentInfo.userId,
                        hospital = commentInfo.hospitalName,
                        name = commentInfo.tempNickname,
                        isAuth = commentInfo.isHospitalAuth
                    ),
                    type = "post",
                    // TODO {* createdAt 어떻게 되는지 확인 *}
                    createdAt = commentInfo.commentCreateAt
                )
                if (commentInfo.userId == myUserId) {
                    Surface(onClick = {}, modifier = Modifier.height(20.heightPercent(context).dp), shape = RoundedCornerShape(4.widthPercent(context).dp), contentColor = Gray100, color = Gray100) {
                        Row(Modifier.height(18.heightPercent(context).dp), Arrangement.Center, Alignment.CenterVertically) {
                            Text(text = "삭제", style = Typography.labelMedium.copy(color = Gray400))
                            Spacer(modifier = Modifier.padding(1.widthPercent(context).dp))
                            Icon(imageVector = Trash, modifier = Modifier.size(18.widthPercent(context).dp), color = Gray400)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
            Text(text = commentInfo.commentContent, style = Typography.displayMedium)
            Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
            if(commentInfo.recomment.size != 0) {
                ClickableText(text = AnnotatedString("답글달기"), style = Typography.labelMedium.copy(color = Gray400)) {
                    Log.d("답글달기 클릭: ", "${commentInfo.commentId} clicked!!!")
                }
            }
        }
    }
    HorizontalDivider()
}
