package com.org.egglog.component.molecules.headers

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.org.egglog.utils.ArrowLeft
import com.org.egglog.component.atoms.buttons.IconButton
import com.org.egglog.component.atoms.inputs.SearchInput
import com.org.egglog.theme.*

@Composable
fun SearchHeader(
    onClickBack : () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite)
//            .border(1.dp, Success500)
    ) {
        Column {
            SearchHeaderContents(
                onClickBack = onClickBack
            )

        }
    }
}


@Composable
fun SearchHeaderContents(
    onClickBack : () -> Unit = {},
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite)
            .padding(horizontal = 10.dp),
//            .border(3.dp, Gray500),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            size = 25.dp,
            imageVector = ArrowLeft,
            color = NaturalBlack,
            onClick = { onClickBack })


        val focusManager = LocalFocusManager.current
        val pin = remember { mutableStateOf("") }
        val text3 = remember { mutableStateOf("") }
        SearchInput(
            text = text3.value,
            onValueChange = { text3.value = it },
            focusManager = focusManager,
            placeholder = "근무지 지역 선택",
            onClickDone = { Log.d("click done: ", text3.value) }
        )

    }
}