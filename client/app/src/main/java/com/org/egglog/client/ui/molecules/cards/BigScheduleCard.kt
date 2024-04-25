package com.org.egglog.client.ui.molecules.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.client.R
import com.org.egglog.client.ui.atoms.cards.BackgroundCard
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.theme.DayCard
import com.org.egglog.client.ui.theme.Error300
import com.org.egglog.client.ui.theme.EveCard
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.NightCard
import com.org.egglog.client.ui.theme.Orange300
import com.org.egglog.client.ui.theme.Pink300
import com.org.egglog.client.ui.theme.Primary400
import com.org.egglog.client.ui.theme.Primary600
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.MoreHoriz
import com.org.egglog.client.utils.widthPercent

@Composable
fun BigScheduleCard(work: String, content: String, startTime: String, endTime: String, onClickMore: () -> Unit) {
    val context = LocalContext.current
    class CardContent(val color: Color, val text: String, val imageName: Any)

    val cardContent: CardContent = when(work) {
        "day" -> CardContent(DayCard, "Day 근무", R.drawable.day)
        "eve" -> CardContent(EveCard, "Eve 근무", R.drawable.eve)
        "night" -> CardContent(NightCard, "Night 근무", R.drawable.night)
        "off" -> CardContent(Primary400, "Off", R.drawable.off)
        "교육" -> CardContent(Orange300, "교육", R.drawable.education)
        "휴가" -> CardContent(Error300, "휴가", R.drawable.vacation)
        "보건" -> CardContent(Pink300, "보건", R.drawable.health)
        else -> CardContent(Gray100, "", R.drawable.dark)
    }

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 12.widthPercent(context).dp, color = cardContent.color, borderRadius = 10.widthPercent(context).dp) {
        Column() {
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier
                            .size(10.widthPercent(context).dp)
                            .background(Color.Transparent, CircleShape)
                            .border(3.dp, Primary600, CircleShape)) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${startTime} - ${endTime}", style = Typography.displayLarge)
                }
                IconButton(onClick = { onClickMore() }, Modifier.size(20.dp)) {
                    Icon(imageVector = MoreHoriz, modifier = Modifier.fillMaxSize())
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
            Column() {
                Text(text = "${cardContent.text}", style = Typography.bodyLarge.copy(fontSize = 18.sp))
                Spacer(modifier = Modifier.height(6.dp))
                    Text("${content}", style = Typography.displayMedium)
                }
                LocalImageLoader(imageUrl = cardContent.imageName, Modifier.size(42.widthPercent(context).dp))
            }
        }
    }
}