package com.org.egglog.component.atoms.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.utils.heightPercent
import com.org.egglog.utils.widthPercent

@Composable
fun HalfBigButton(modifier: Modifier = Modifier, colors: ButtonColors, onClick: () -> Unit, enabled: Boolean = true, content: @Composable () -> Unit) {
    BasicButton(
        modifier = modifier.width(142.widthPercent(LocalContext.current).dp).height(50.heightPercent(LocalContext.current).dp),
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(8.widthPercent(LocalContext.current).dp),
        enabled = enabled
    ) {
        content()
    }
}