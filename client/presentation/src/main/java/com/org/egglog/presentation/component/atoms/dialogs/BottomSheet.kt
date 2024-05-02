package com.org.egglog.presentation.component.atoms.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.org.egglog.presentation.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    height: Dp,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    sheetContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
//        modifier = Modifier.fillMaxHeight(),
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
            Button(onClick = {
                scope.launch { bottomSheetState.hide() }
            }) {
                Text("이렇게 숨겨지면 배신")
            }
        }
    }
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