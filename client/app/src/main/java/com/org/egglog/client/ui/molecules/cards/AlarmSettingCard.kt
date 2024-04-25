package com.org.egglog.client.ui.molecules.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.client.R
import com.org.egglog.client.ui.atoms.cards.BackgroundCard
import com.org.egglog.client.ui.theme.BlueLight200
import com.org.egglog.client.ui.theme.DayCard
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.widthPercent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.atoms.toggle.Toggle

class CardContent(val title: String, val color: Color, val imageName: Any, val time: String, val duration: Number, val interval: Number)

@Composable
fun AlarmSettingCard(cardContent: CardContent, checked: Boolean, setToggle: () -> Unit) {
    val context = LocalContext.current

    val hour = cardContent.time.substringBefore(":").toInt()

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 10.widthPercent(context).dp, color = cardContent.color, borderRadius = 10.widthPercent(context).dp) {
        Column() {
            Row(    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                Text("${cardContent.title}", style = Typography.bodyMedium.copy(fontSize = 18.sp))
                Toggle(checked = checked) {
                    setToggle()
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${if(hour in 0..11) "오전" else "오후"} ${cardContent.time} | ${cardContent.duration}분 간 | ${cardContent.interval}분 간격", style = Typography.displayLarge)
                LocalImageLoader(imageUrl = cardContent.imageName, Modifier.size(44.dp))
            }
        }
    }
}