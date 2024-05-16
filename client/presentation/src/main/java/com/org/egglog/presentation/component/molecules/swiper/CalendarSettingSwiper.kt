package com.org.egglog.presentation.component.molecules.swiper

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.theme.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun CalendarSettingSwiper(onClick: () -> Unit, deleteEnabled: Boolean, enabled: Boolean = true, children: @Composable () -> Unit) {
    val swipeAbleState = rememberSwipeableState(initialValue = 0)
    val coroutineScope = rememberCoroutineScope()
    val squareSize = 70.widthPercent(LocalContext.current).dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.heightPercent(LocalContext.current).dp)
            .swipeable(
                state = swipeAbleState,
                orientation = Orientation.Horizontal,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                velocityThreshold = 1000.dp,
                enabled = enabled
            ),
        contentAlignment = Alignment.Center
    ) {
        // 버튼 컴포즈
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            TextButton(
                enabled = true,
                modifier = Modifier
                    .width(70.widthPercent(LocalContext.current).dp)
                    .fillMaxHeight()
                    .background(if(deleteEnabled) Gray300 else Warning300),
                onClick = {
                    coroutineScope.launch {
                        swipeAbleState.animateTo(0, tween(600, 0))
                    }
                    onClick()
                }) {
                if(deleteEnabled) Text("삭제", color= NaturalWhite) else Text(text = "캘린더\n내보내기", color = NaturalWhite, textAlign = TextAlign.Center)
            }
        }

        // 카드 컴포즈
        Box(modifier = Modifier
            .offset{IntOffset(swipeAbleState.offset.value.roundToInt(), 0)}
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(NaturalWhite),
                contentAlignment = Alignment.CenterStart) {
                children()
            }
        }
    }
}