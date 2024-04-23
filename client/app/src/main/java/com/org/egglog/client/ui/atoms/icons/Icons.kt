package com.org.egglog.client.ui.atoms.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun Icon(imageVector: ImageVector, modifier: Modifier) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier
    )
}