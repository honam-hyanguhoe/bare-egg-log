package com.org.egglog.presentation.component.atoms.cards

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
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.imageLoader.UrlImageLoader
import com.org.egglog.presentation.theme.Typography

@Composable
fun ProfileCard(userDetail: UserDetail?) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UrlImageLoader(imageUrl = userDetail?.profileImgUrl, modifier = Modifier.size(50.widthPercent(context).dp).clip(CircleShape))
        Text(text = userDetail?.userName ?: "error", style = Typography.bodyLarge)
        Text(text = "(${userDetail?.empNo ?: "error"})", style = Typography.labelMedium)
        Text(text = userDetail?.email ?: "error", style = Typography.displayMedium)
    }
}