package com.org.egglog.client.ui.atoms.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.org.egglog.client.ui.theme.NaturalBlack

@Composable
fun Icon(imageVector: ImageVector, modifier: Modifier, color: Color = NaturalBlack) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier,
        tint = color
    )
}