package com.org.egglog.client.ui.atoms.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.theme.Gray300
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent

@Composable
fun SettingButton(onClick: () -> Unit, icon: ImageVector, text: String, color: Color) {
    val context = LocalContext.current
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.heightPercent(context).dp)
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                modifier = Modifier.size(22.widthPercent(context).dp),
                color = color
            )
            Spacer(Modifier.padding(2.widthPercent(context).dp))
            Text(style = Typography.bodyMedium, text = text, color = color)
        }
    }
    HorizontalDivider(color = Gray300)
}