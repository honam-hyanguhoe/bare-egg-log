package com.org.egglog.presentation.component.atoms.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.theme.Typography

@Composable
fun SettingButton(onClick: () -> Unit, icon: ImageVector, text: String, color: Color, iconColor: Color = color) {
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
                modifier = Modifier.size(20.widthPercent(context).dp),
                color = iconColor
            )
            Spacer(Modifier.padding(2.widthPercent(context).dp))
            Text(style = Typography.bodyMedium, text = text, color = color)
        }
    }
    HorizontalDivider(color = Gray300)
}