package com.org.egglog.presentation.component.atoms.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning200
import com.org.egglog.presentation.utils.heightPercent

@Composable
fun BannerCard() {
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .height(80.heightPercent(context).dp)
            .background(Warning200),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.padding(vertical = 14.dp, horizontal = 10.dp)) {
            Text(text = "에그로그에 친구를 초대하고,", style = Typography.displayLarge)
            Text(
                text = "친구와 함께 일정을 공유하세요!",
                style = Typography.displayLarge
            )
        }

        Column(
            Modifier
                .fillMaxHeight()
                .height(70.heightPercent(context).dp)
                .padding(top = 10.dp, end = 14.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            LocalImageLoader(
                imageUrl = R.drawable.dark,
                Modifier.size(60.dp)
            )
        }
    }
}