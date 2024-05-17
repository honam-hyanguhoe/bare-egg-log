package com.org.egglog.presentation.component.atoms.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.org.egglog.presentation.component.atoms.icons.Icon

@Composable
fun IconButton(size: Dp, imageVector: ImageVector, color: Color, onClick: () -> Unit, enabled: Boolean = false, backgroundColor: Color = Color.Transparent) {
    Surface(
        modifier = Modifier.size(size),
        onClick = onClick,
        shape = CircleShape,
        enabled = enabled,
        color = backgroundColor,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        Icon(modifier = Modifier.size(size), imageVector = imageVector, color = color)
    }
}

@Composable
fun CustomIconButton(size: Dp, imageVector: ImageVector, color: Color, onClick: () -> Unit, enabled: Boolean = true) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(size),
        enabled = enabled
    ) {
        Icon(
            imageVector = imageVector,
            modifier = Modifier,
            color = color
        )
    }
}
