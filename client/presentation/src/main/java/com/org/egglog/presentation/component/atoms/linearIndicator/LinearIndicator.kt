package com.org.egglog.presentation.component.atoms.linearIndicator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.*

@Composable
fun LinearIndicator(
    step: Float = 0f,
    isLoading: Boolean = false,
) {
    var currentProgress by remember { mutableStateOf(step) }
    var loading by remember { mutableStateOf(isLoading) }
    val scope = rememberCoroutineScope()

    val animatedProgress = animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = androidx.compose.animation.core.spring()
    )

//    LaunchedEffect(loading) {
//        if (loading) {
//            scope.launch {
//                while (currentProgress < 1f) {
//                    currentProgress += 0.01f
//                    delay(20) // 진행률 업데이트 지연 시간
//                    if (currentProgress >= 1f) loading = false
//                }
//            }
//        }
//    }

    if (loading) {
        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier
                .fillMaxWidth()
                .size(1.3.dp),
            color = Gray500,
            trackColor = Gray300,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
        )
    }
}

/** Iterate the progress value */
//suspend fun loadProgress(updateProgress: (Float) -> Unit) {
//    for (i in 1..2) {
//        updateProgress(i.toFloat() / 2)
//        delay(100)
//    }
//}