package com.org.egglog.presentation.component.atoms.dialogs

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    height: Dp,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    sheetContent: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val isKeyboardOpen by keyboardAsState()

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        containerColor = White,
        contentColor = NaturalBlack,
        scrimColor = NaturalBlack.copy(alpha = 0.6F),
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            sheetContent()
        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val context = LocalContext.current
    val rootView = LocalView.current
    val isKeyboardOpen = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - r.bottom
            isKeyboardOpen.value = keypadHeight > screenHeight * 0.15
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return isKeyboardOpen
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewDarkenBottomSheet() {
//    InteractiveBottomSheet(
//        height = 380.dp,
//        padding = 5.dp,
//        sheetContent = { SheetContent() },
//
//    )
//}