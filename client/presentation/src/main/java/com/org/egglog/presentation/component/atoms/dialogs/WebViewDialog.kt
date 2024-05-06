package com.org.egglog.presentation.component.atoms.dialogs

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.org.egglog.presentation.component.organisms.webView.FullPageWebView
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun WebViewDialog(
    url: String = "https://www.egg-log.org/agreement",
    onDismiss: () -> Unit,
    context : Context,
    width : Int = 400,
    height  : Int = 700
) {
    val widthP = width.widthPercent(context)
    val heightP = height.heightPercent(context)
    Log.d("webview", widthP.toString())
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(widthP.dp)
                .height(heightP.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            FullPageWebView(url = url,  onClose = onDismiss, height = heightP.toFloat(), width = widthP.toFloat())
        }
    }
}


