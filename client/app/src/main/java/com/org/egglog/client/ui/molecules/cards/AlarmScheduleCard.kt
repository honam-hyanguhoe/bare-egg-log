package com.org.egglog.client.ui.molecules.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.client.R
import com.org.egglog.client.ui.atoms.cards.BackgroundCard
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.utils.widthPercent
import com.org.egglog.client.ui.theme.BlueLight200
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.heightPercent

@Composable
fun AlarmScheduleCard(title: String, time: String, duration: Number, interval: Number) {
    val context = LocalContext.current

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 12.widthPercent(context).dp, color = BlueLight200, borderRadius = 10.widthPercent(context).dp) {
        Column() {
            Text(text = "${title}", style = Typography.bodyLarge.copy(fontSize = 18.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${time} | ${duration}분 간 | ${interval}분 간격", style = Typography.displayMedium)
            }
        }
    }
}

@Preview
@Composable
fun AlarmCardPreview() {
    MaterialTheme {
        AlarmScheduleCard(title="기상 알람", time="11:00", duration=30, interval=5)
    }
}