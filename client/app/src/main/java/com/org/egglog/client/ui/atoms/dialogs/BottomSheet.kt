package com.org.egglog.client.ui.atoms.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.Gray300
import com.org.egglog.client.ui.theme.Gray500
import com.org.egglog.client.ui.theme.Gray700
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.White
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