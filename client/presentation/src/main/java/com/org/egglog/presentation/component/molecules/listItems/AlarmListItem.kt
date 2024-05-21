package com.org.egglog.presentation.component.molecules.listItems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.toggle.Toggle
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent

@Composable
fun AlarmListItem(
    title: String,
    body: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    toggleEnabled: Boolean = true
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 2.widthPercent(context).dp,
                vertical = 12.heightPercent(context).dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                Text(text = title, style = Typography.bodyLarge)
                Spacer(modifier = Modifier.padding(2.widthPercent(context).dp))
                Text(text = body, style = Typography.displayMedium.copy(color = Gray500))
            }
            Toggle(
                toggleEnabled = toggleEnabled,
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
    HorizontalDivider(color = Gray300)
}