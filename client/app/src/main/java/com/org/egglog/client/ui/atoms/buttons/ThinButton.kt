package com.org.egglog.client.ui.atoms.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent

@Composable
fun ThinButton(modifier: Modifier = Modifier, colors: ButtonColors, onClick: () -> Unit, enabled: Boolean = true, content: @Composable () -> Unit) {
    BasicButton(
        modifier = modifier.width(142.widthPercent(LocalContext.current).dp).height(36.heightPercent(
            LocalContext.current).dp),
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(5.widthPercent(LocalContext.current).dp),
        enabled = enabled
    ) {
        content()
    }
}