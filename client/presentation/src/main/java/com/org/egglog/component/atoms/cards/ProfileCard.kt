package com.org.egglog.component.atoms.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.data.UserInfo
import com.org.egglog.utils.widthPercent
import com.org.egglog.component.atoms.imageLoader.UrlImageLoader
import com.org.egglog.theme.Typography


@Composable
fun ProfileCard(userInfo: UserInfo) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UrlImageLoader(imageUrl = userInfo.profileImgUrl, modifier = Modifier.size(50.widthPercent(context).dp).clip(CircleShape))
        Text(text = userInfo.userName, style = Typography.bodyLarge)
        Text(text = "(${userInfo.empNo})", style = Typography.labelMedium)
        Text(text = userInfo.userEmail, style = Typography.displayMedium)
    }
}