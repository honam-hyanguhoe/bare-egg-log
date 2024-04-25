package com.org.egglog.client.ui.molecules.listItems

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.toggle.Toggle
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.theme.Warning400
import com.org.egglog.client.utils.widthPercent

@Composable
fun AlarmListItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.width(320.widthPercent(context).dp)
    ) {
        Row(
            modifier = Modifier.width(320.widthPercent(context).dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = Typography.displayMedium)
            Toggle(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}