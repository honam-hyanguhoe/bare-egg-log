package com.org.egglog.client.ui.molecules.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.client.R
import com.org.egglog.client.ui.atoms.cards.BackgroundCard
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.utils.widthPercent
import com.org.egglog.client.ui.theme.Day
import com.org.egglog.client.ui.theme.Eve
import com.org.egglog.client.ui.theme.Night
import com.org.egglog.client.ui.theme.Off
import com.org.egglog.client.ui.theme.Education
import com.org.egglog.client.ui.theme.Vacation
import com.org.egglog.client.ui.theme.Health
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.MoreHoriz

@Composable
fun SmallScheduleCard(work: String) {
    val context = LocalContext.current
    
    class CardContent(val color: Color, val text: String, val imageName: Any)

    val cardContent: CardContent = when(work) {
        "day" -> CardContent(Day, "Day 근무", R.drawable.day)
        "eve" -> CardContent(Eve, "Eve 근무", R.drawable.eve)
        "night" -> CardContent(Night, "Night 근무", R.drawable.night)
        "off" -> CardContent(Off, "Off", R.drawable.off)
        "교육" -> CardContent(Education, "교육", R.drawable.education)
        "휴가" -> CardContent(Vacation, "휴가", R.drawable.vacation)
        "보건" -> CardContent(Health, "보건", R.drawable.health)
        else -> CardContent(Gray100, "", R.drawable.dark)
    }
    

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 12.widthPercent(context).dp, color = cardContent.color, borderRadius = 10.widthPercent(context).dp) {
        Column() {
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "14:00 - 20:00", style = Typography.displayLarge)
                    Icon(imageVector = MoreHoriz, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${cardContent.text}", style = Typography.bodyLarge.copy(fontSize = 18.sp))
                LocalImageLoader(imageUrl = cardContent.imageName, Modifier.size(36.dp))
            }
        }
    }
}