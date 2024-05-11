package com.org.egglog.presentation.component.molecules.cards

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.CommentInfo
import com.org.egglog.client.data.Profile
import com.org.egglog.presentation.utils.ArrowRecommend
import com.org.egglog.presentation.utils.Trash
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.profileItem.ProfileItem
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.CalculateTimeDifference

@Composable
fun CommentCard(isRecomment: Boolean = false, commentInfo: CommentInfo, myUserId: Long, onDeleteClick: (Long) -> Unit, onRecommentClick: (Long) -> Unit) {
    val context = LocalContext.current
    Row(
        Modifier
            .background(color = NaturalWhite)
            .padding(8.widthPercent(context).dp, 14.heightPercent(context).dp), Arrangement.Start, Alignment.CenterVertically) {
        if(isRecomment) {
            Icon(imageVector = ArrowRecommend, modifier = Modifier.size(18.widthPercent(context).dp), color = Gray600)
            Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
        }
        Column {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                ProfileItem(
                    profile = Profile(
                        myUserId,
                        hospital = commentInfo.hospitalName,
                        name = commentInfo.tempNickname,
                        isAuth = commentInfo.isHospitalAuth,
                        commentUserId = commentInfo.userId
                    ),
                    type = "post",
                    // TODO {* createdAt 어떻게 되는지 확인 *}
                    createdAt = CalculateTimeDifference(commentInfo.commentCreateAt)
                )
                if (commentInfo.userId == myUserId) {
                    Surface(onClick = { onDeleteClick(commentInfo.commentId) }, modifier = Modifier.height(20.heightPercent(context).dp), shape = RoundedCornerShape(4.widthPercent(context).dp), contentColor = NaturalWhite, color = NaturalWhite) {
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
            if(!isRecomment) {
                ClickableText(text = AnnotatedString("답글달기"), style = Typography.labelMedium.copy(color = Gray400)) {
                    onRecommentClick(commentInfo.commentId)
                }
            }
        }
    }
    HorizontalDivider()
}