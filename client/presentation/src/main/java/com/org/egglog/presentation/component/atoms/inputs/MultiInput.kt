package com.org.egglog.presentation.component.atoms.inputs

import androidx.compose.foundation.layout.height
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
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun MultiInput(
    text: String,
    placeholder: String,
    height : Int = 300,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    val context = LocalContext.current
    OutlinedTextField(
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .height(height.heightPercent(context).dp),
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
        keyboardOptions = keyboardOptions
    )
}