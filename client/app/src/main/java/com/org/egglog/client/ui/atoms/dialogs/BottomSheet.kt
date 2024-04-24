package com.org.egglog.client.ui.atoms.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.White
import kotlinx.coroutines.launch

// 화면의 나머지 부분과 독립적.
// 어둡게 dim 처리
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet() {
    val BottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = { showBottomSheet = false },
        sheetState = BottomSheetState,
        containerColor = White,
        contentColor = NaturalBlack,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        scrimColor = Gray100,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveBottomSheet() {
    val scope = rememberCoroutineScope()

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    );
    val iBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        sheetContent = {
            SheetContent()
        },
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        scaffoldState = iBottomSheetState,
        sheetPeekHeight = 500.dp,
        sheetContainerColor = White,
        sheetContentColor = NaturalBlack,
        containerColor = White,
        contentColor = NaturalBlack,
        sheetSwipeEnabled = false,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "나오나?")
            Button(
                onClick = {
                    scope.launch { iBottomSheetState.bottomSheetState.expand() }
                }
            ) {
                Text("Click to collapse sheet")
            }
        }
    }
}

@Composable
fun SheetContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(50) {
                Text(text = "test")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkenBottomSheet() {
    InteractiveBottomSheet()
}