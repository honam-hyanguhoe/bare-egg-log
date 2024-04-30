package com.org.egglog.component.atoms.inputs

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.component.atoms.icons.Icon
import com.org.egglog.theme.*
import com.org.egglog.utils.Search
import com.org.egglog.utils.widthPercent

@Composable
fun SearchInput(
    text: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onClickDone: () -> Unit
) {
    val context = LocalContext.current
    OutlinedTextField(
        modifier = Modifier
            .width(320.widthPercent(context).dp),
        shape = RoundedCornerShape(8.widthPercent(context).dp),
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = Gray300,
            unfocusedContainerColor = White,
            unfocusedTextColor = NaturalBlack,
            unfocusedTrailingIconColor = White,
            focusedPlaceholderColor = Gray300,
            focusedContainerColor = White,
            focusedTextColor = NaturalBlack,
            focusedTrailingIconColor = White,
            unfocusedIndicatorColor = Gray400,
            focusedIndicatorColor = Warning400
        ),
        value = text,
        onValueChange = onValueChange,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onClickDone()
            }
        ),
        placeholder = { Text(placeholder) },
        trailingIcon = { Icon(imageVector = Search, modifier = Modifier.size(24.widthPercent(context).dp), color = Gray300) },
        singleLine = true,
        keyboardOptions = keyboardOptions
    )
}