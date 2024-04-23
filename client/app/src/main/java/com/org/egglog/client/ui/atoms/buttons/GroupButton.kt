package com.org.egglog.client.ui.atoms.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent
import com.org.egglog.client.ui.theme.*
import com.org.egglog.client.R
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.utils.ArrowRight

@Composable
fun GroupButton(onClick: () -> Unit, groupName: String, memberCnt: Int, groupMaster: String, groupImage: Int, groupId: Int) {
    val context = LocalContext.current
    val image = when(groupImage) {
        1 -> R.drawable.dark
        2 -> R.drawable.dark
        3 -> R.drawable.dark
        else -> R.drawable.cry
    }
    Surface(
        onClick = onClick,
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .height(70.heightPercent(context).dp),
        color = Gray100,
        shadowElevation = 4.heightPercent(context).dp,
        shape = RoundedCornerShape(10.widthPercent(context).dp)
    ) {
        Row(Modifier.fillMaxSize().padding(10.widthPercent(context).dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(32.widthPercent(context).dp)
            )
            Column(Modifier.width(212.widthPercent(context).dp), Arrangement.Center) {
                Text(style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold), text = "$groupName 모여라")
                Text(style = Typography.displayMedium,  text = "멤버 $memberCnt · 그룹장 $groupMaster")
            }
            Icon(imageVector = ArrowRight, modifier = Modifier.size(24.widthPercent(context).dp), color = Gray300)
        }
    }
}