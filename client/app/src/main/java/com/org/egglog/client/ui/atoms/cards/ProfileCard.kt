package com.org.egglog.client.ui.atoms.cards

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
import com.org.egglog.client.ui.atoms.imageLoader.UrlImageLoader
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.widthPercent

@Composable
fun ProfileCard(profileImgUrl: String, userName: String, empNo: String, userEmail: String, userId: Int) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UrlImageLoader(imageUrl = profileImgUrl, modifier = Modifier.size(50.widthPercent(context).dp).clip(CircleShape))
        Text(text = userName, style = Typography.bodyLarge)
        Text(text = "($empNo)", style = Typography.labelMedium)
        Text(text = userEmail, style = Typography.displayMedium)
    }
}