package com.org.egglog.client.ui.atoms.buttons

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.imageLoader.UrlImageLoader
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent

@Composable
fun ProfileButton(onClick: () -> Unit, profileImgUrl: String, userId: Int, userName: String, isSelected: Boolean, isMine: Boolean){
    val context = LocalContext.current
    val name = if (isMine) "$userName(나)" else userName
    Surface(
        onClick = onClick,
        modifier = Modifier.size(80.widthPercent(context).dp, 116.heightPercent(context).dp),
        shape = RoundedCornerShape(34.widthPercent(context).dp),
        color = if(isSelected) Gray200 else NaturalWhite
    ) {
        Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            UrlImageLoader(imageUrl = profileImgUrl, modifier = Modifier
                .size(50.widthPercent(LocalContext.current).dp)
                .clip(CircleShape))
            Spacer(Modifier.padding(6.heightPercent(context).dp))
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
                Text(style = Typography.bodyMedium, text = name)
            }
        }
    }
}