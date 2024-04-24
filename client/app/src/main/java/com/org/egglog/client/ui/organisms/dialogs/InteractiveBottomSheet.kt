package com.org.egglog.client.ui.organisms.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.White
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveBottomSheet(
    height: Dp,
    padding: Dp,
) {
    val scope = rememberCoroutineScope()

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    );

    val iBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        sheetContent = {
            SheetContent()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        scaffoldState = iBottomSheetState,
        sheetPeekHeight = height,
        sheetContainerColor = White,
        sheetContentColor = NaturalBlack,
        containerColor = White,
        contentColor = NaturalBlack,
        sheetSwipeEnabled = false,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column() {
                Button(
                    onClick = {
                        scope.launch { iBottomSheetState.bottomSheetState.partialExpand() }
                    }
                ) {
                    Text("열어줘")
                }
                Button(
                    onClick = {
                        scope.launch { iBottomSheetState.bottomSheetState.hide() }
                    }
                ) {
                    Text("닫을래")
                }
            }
        }
    }
}

@Composable
fun SheetContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(width = 1.dp, color = NaturalBlack),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "test")
    }
}