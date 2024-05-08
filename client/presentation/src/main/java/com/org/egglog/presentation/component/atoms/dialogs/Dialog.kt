package com.org.egglog.presentation.component.atoms.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.atoms.buttons.BasicButton
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            BasicButton(
                onClick = { onConfirmation() },
                modifier = Modifier.fillMaxWidth(0.485f),
                colors = ButtonColors(contentColor = NaturalWhite, containerColor = Warning300, disabledContentColor = NaturalWhite, disabledContainerColor = Gray300),
                shape = RoundedCornerShape(24.widthPercent(LocalContext.current).dp)
            ) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            BasicButton(
                onClick = { onDismissRequest() },
                modifier = Modifier.fillMaxWidth(0.485f),
                colors = ButtonColors(contentColor = NaturalWhite, containerColor = Gray300, disabledContentColor = NaturalWhite, disabledContainerColor = Gray300),
                shape = RoundedCornerShape(24.widthPercent(LocalContext.current).dp)
            ) {
                Text(text = "취소")
            }
        },
        title = { Text(text = dialogTitle) },
        text = {
            Column {
                Text(text = dialogText)
            }
        },
        containerColor = White,
        textContentColor = NaturalBlack,
        titleContentColor = NaturalBlack
    )
}

@Preview
@Composable
fun preview() {
    ClientTheme {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            onConfirmation = { /*TODO*/ },
            dialogTitle = "",
            dialogText = ""
        )
    }
}