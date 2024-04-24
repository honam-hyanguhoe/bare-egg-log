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
fun MiddleButton(modifier: Modifier = Modifier, colors: ButtonColors, onClick: () -> Unit, content: @Composable () -> Unit) {
    BasicButton(
        modifier = modifier.width(320.widthPercent(LocalContext.current).dp).height(48.heightPercent(LocalContext.current).dp),
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(10.widthPercent(LocalContext.current).dp)
    ) {
        content()
    }
}