package com.org.egglog.client.ui.atoms.buttons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BasicButton(modifier: Modifier, onClick: () -> Unit, shape: RoundedCornerShape, colors: ButtonColors, enabled: Boolean = true, content: @Composable () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContainerColor
        ),
        enabled = enabled
    ) {
        content()
    }
}