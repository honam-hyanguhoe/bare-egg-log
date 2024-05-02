package com.org.egglog.presentation.component.atoms.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.Close
import com.org.egglog.presentation.utils.Edit
import com.org.egglog.presentation.theme.*

@Composable
fun FloatingButton(
        onClick: () -> Unit,
        onWorkClick: () -> Unit,
        onPersonalClick: () -> Unit,
        onSettingClick: () -> Unit,
        horizontalPadding : Dp,
        verticalPadding : Dp,
) {
    val fabInteractionSource = remember {
        MutableInteractionSource()
    }
    val (isVisible, setVisible) = remember { mutableStateOf(false) }

    Column(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                    .border(1.dp, NaturalBlack),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
        ) {
            ExtendedFab(
                    onWorkClick,
                    onPersonalClick,
                    onSettingClick
            )
        }

        FloatingActionButton(
                onClick = {
                    onClick()
                    println("isVisible $isVisible")
                    setVisible(!isVisible)
                },
                modifier = Modifier
                        .padding(5.dp),
                shape = FloatingActionButtonDefaults.largeShape,
                containerColor = BlueGray900,
                contentColor = NaturalWhite,
                elevation = FloatingActionButtonDefaults.elevation(),
                interactionSource = fabInteractionSource
        ) {
            Icon(imageVector = if (isVisible) Close else Edit, contentDescription = "fab Icon")
        }
    }

}

@Composable
fun ColumnScope.ExtendedFab(
        onWorkClick: () -> Unit,
        onPersonalClick: () -> Unit,
        onSettingClick: () -> Unit
) {
    Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
                onClick = { onWorkClick() },
                modifier = Modifier
                        .height(40.dp)
                        .width(90.dp),
                shape = FloatingActionButtonDefaults.extendedFabShape,
                containerColor = BlueGray900,
                contentColor = NaturalWhite,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
        ) {
            Text(text = "근무 일정 추가")
        }
        FloatingActionButton(
                onClick = { onPersonalClick() },
                modifier = Modifier
                        .height(40.dp)
                        .width(90.dp),
                shape = FloatingActionButtonDefaults.extendedFabShape,
                containerColor = BlueGray900,
                contentColor = NaturalWhite,
                elevation = FloatingActionButtonDefaults.elevation(),
        ) {
            Text(text = "개인 일정 추가")
        }
        FloatingActionButton(
                onClick = { onSettingClick() },
                modifier = Modifier
                        .height(40.dp)
                        .width(90.dp),
                shape = FloatingActionButtonDefaults.extendedFabShape,
                containerColor = BlueGray900,
                contentColor = NaturalWhite,
                elevation = FloatingActionButtonDefaults.elevation(),
        ) {
            Text(text = "근무 설정", textAlign = TextAlign.End)
        }
    }
}
