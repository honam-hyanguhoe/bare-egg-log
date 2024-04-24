package com.org.egglog.client.ui.molecules.swiper

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.Warning300
import com.org.egglog.client.ui.theme.Gray300
import com.org.egglog.client.utils.heightPercent
import com.org.egglog.client.utils.widthPercent
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun Swiper(onDelete: () -> Unit, onChangeLeader: () -> Unit, children: @Composable () -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val coroutineScope = rememberCoroutineScope()
    val squareSize = 120.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    Box(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(56.heightPercent(LocalContext.current).dp)
                    .swipeable(
                            state = swipeableState,
                            orientation = Orientation.Horizontal,
                            anchors = anchors,
                            thresholds = { _, _ -> FractionalThreshold(0.5f) },
                            velocityThreshold = 1000.dp
                    ),
            contentAlignment = Alignment.Center
    ) {
        // 버튼 컴포즈
        Box(
                modifier = Modifier
                        .align(Alignment.CenterEnd)
        ) {
            Row() {
                TextButton(
                        modifier = Modifier
                                .width(54.widthPercent(LocalContext.current).dp)
                                .fillMaxHeight()
                                .background(Warning300),
                        onClick = {
                            coroutineScope.launch {
                                swipeableState.animateTo(0, tween(600, 0))
                            }
                            onChangeLeader()
                        }
                ) {
                        Text("모임장\n위임", color = NaturalWhite, textAlign = TextAlign.Center)
                }
                TextButton(
                        modifier = Modifier
                                .width(54.widthPercent(LocalContext.current).dp)
                                .fillMaxHeight()
                                .background(Gray300),
                        onClick = {
                            coroutineScope.launch {
                                swipeableState.animateTo(0, tween(600, 0))
                            }
                            onDelete()
                        }) {
                    Text("삭제", color= NaturalWhite)
                }
            }
        }

        // 카드 컴포즈
        Box(modifier = Modifier
                .offset{IntOffset(swipeableState.offset.value.roundToInt(), 0)}
        ) {
            Box(modifier = Modifier.fillMaxSize().background(NaturalWhite).padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.CenterStart) {
                children()
            }
        }
    }
}