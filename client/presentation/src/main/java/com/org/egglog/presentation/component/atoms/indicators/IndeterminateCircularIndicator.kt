package com.org.egglog.presentation.component.atoms.indicators

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.Gray500
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun IndeterminateCircularIndicator(loading: Boolean) {
    val context = LocalContext.current
    if (!loading) return
    CircularProgressIndicator(
        modifier = Modifier.width(40.widthPercent(context).dp),
        color = Warning300,
        trackColor = Gray500,
    )
}