package com.org.egglog.presentation.component.atoms.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.UserInfo
import com.org.egglog.domain.group.model.Member
import com.org.egglog.presentation.component.atoms.imageLoader.UrlImageLoader
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun ProfileButton(onClick: () -> Unit, userInfo: UserInfo, isSelected: Boolean, isMine: Boolean){
    val context = LocalContext.current
    val name = if (isMine) "${userInfo.userName}(나)" else userInfo.userName
    Surface(
        onClick = onClick,
        modifier = Modifier.size(62.widthPercent(context).dp, 108.heightPercent(context).dp),
        shape = RoundedCornerShape(24.widthPercent(context).dp),
        color = if(isSelected) Gray200 else Color.Transparent
    ) {
        Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            UrlImageLoader(imageUrl = userInfo.profileImgUrl, modifier = Modifier
                .size(50.widthPercent(LocalContext.current).dp)
                .clip(CircleShape))
            Spacer(Modifier.padding(4.heightPercent(context).dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                if(isMine) {
                    Box(
                        modifier = Modifier
                            .size(5.widthPercent(LocalContext.current).dp)
                            .border(width = 0.dp, color = Warning300, shape = CircleShape)
                            .background(Warning300, CircleShape)
                    )
                    Spacer(Modifier.padding(2.widthPercent(context).dp))
                }
                Text(style = Typography.bodySmall, text = name)
            }
        }
    }
}

@Composable
fun GroupProfileButton(onClick: () -> Unit, userInfo: Member, isSelected: Boolean, isMine: Boolean){
    val context = LocalContext.current
    val name = if (isMine) "${userInfo.userName}(나)" else userInfo.userName
    Surface(
        onClick = onClick,
        modifier = Modifier.size(70.widthPercent(context).dp, 108.heightPercent(context).dp),
        shape = RoundedCornerShape(24.widthPercent(context).dp),
        color = if(isSelected) Gray200 else Color.Transparent
    ) {
        Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            UrlImageLoader(imageUrl = userInfo.profileImgUrl, modifier = Modifier
                .size(50.widthPercent(LocalContext.current).dp)
                .clip(CircleShape))
            Spacer(Modifier.padding(4.heightPercent(context).dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                if(isMine) {
                    Box(
                        modifier = Modifier
                            .size(5.widthPercent(LocalContext.current).dp)
                            .border(width = 0.dp, color = Warning300, shape = CircleShape)
                            .background(Warning300, CircleShape)
                    )
                    Spacer(Modifier.padding(2.widthPercent(context).dp))
                }
                Text(style = Typography.bodySmall, text = name)
            }
        }
    }
}