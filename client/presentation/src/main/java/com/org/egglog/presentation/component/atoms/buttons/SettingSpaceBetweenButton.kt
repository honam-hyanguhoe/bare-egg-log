package com.org.egglog.presentation.component.atoms.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.Gray200
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.utils.heightPercent


@Composable
fun SettingSpaceBetweenButton(text: String, onClick: () -> Unit, enabled: Boolean = true, content: @Composable () -> Unit) {
    val context = LocalContext.current
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.heightPercent(context).dp)
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(text = text, style = Typography.bodyMedium)
            content()
        }
    }
    HorizontalDivider(color = Gray200)
}