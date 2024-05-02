package com.org.egglog.presentation.component.atoms.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.theme.*

@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(onClick = { onConfirmation() }, modifier = Modifier.width(150.dp)) {
                Text(text = "확인")
            }
        },
        title = { Text(text = dialogTitle) },
        text = {
            Column {
                Text(text = dialogText)
                Text(text = dialogText)
            }
        },
        containerColor = White,
        textContentColor = NaturalBlack,
        titleContentColor = NaturalBlack
    )
}

