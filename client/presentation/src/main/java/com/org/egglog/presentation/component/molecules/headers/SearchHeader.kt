package com.org.egglog.presentation.component.molecules.headers

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
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.utils.ArrowLeft
import com.org.egglog.presentation.component.atoms.inputs.SearchInput
import com.org.egglog.presentation.theme.*

@Composable
fun SearchHeader(
    onClickBack : () -> Unit = {},
    placeholder: String,
    onClickDone: () -> Unit = {},
    searchWord: String,
    onChangeSearch: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NaturalWhite)
//            .border(1.dp, Success500)
    ) {
        Column {
            SearchHeaderContents(
                onClickBack = onClickBack,
                placeholder = placeholder,
                onClickDone = onClickDone,
                searchWord = searchWord,
                onChangeSearch = onChangeSearch
            )

        }
    }
}


@Composable
fun SearchHeaderContents(
    onClickBack : () -> Unit = {},
    placeholder: String,
    onClickDone: () -> Unit = {},
    searchWord: String,
    onChangeSearch: (String) -> Unit
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
        CustomIconButton(
            size = 25.dp,
            imageVector = ArrowLeft,
            color = NaturalBlack,
            onClick = onClickBack)


        val focusManager = LocalFocusManager.current

        SearchInput(
            modifier = Modifier.fillMaxWidth(),
            text = searchWord,
            onValueChange = onChangeSearch,
            focusManager = focusManager,
            placeholder = placeholder,
            onClickDone = onClickDone
        )

    }
}