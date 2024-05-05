package com.org.egglog.presentation.component.atoms.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun SingleInput(
    modifier: Modifier,
    text: String,
        placeholder: String,
        onValueChange: (String) -> Unit,
    focusManager: FocusManager,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    val context = LocalContext.current
    OutlinedTextField(
        modifier = modifier,
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
            }, 
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        ),
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = keyboardOptions
    )
}