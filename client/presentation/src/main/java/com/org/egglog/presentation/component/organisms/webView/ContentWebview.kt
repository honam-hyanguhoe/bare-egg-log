package com.org.egglog.presentation.component.organisms.webView

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.org.egglog.presentation.domain.main.AndroidBridge
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import java.nio.charset.StandardCharsets

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ContentWebView(
    width: Int = 300, height: Int = 250, url: String = "https://www.egg-log.org/", data: String = ""
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            // WebView settings
            settings.apply {
                allowContentAccess = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                useWideViewPort = true
                loadWithOverviewMode = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            }

            settings.javaScriptEnabled = true
            settings.setSupportZoom(false)
            settings.displayZoomControls = false
            settings.builtInZoomControls = false

            setLayerType(
                View.LAYER_TYPE_HARDWARE, null
            )
            webViewClient = WebViewClient()
        }
    }

    webView.addJavascriptInterface(AndroidBridge(context, webView), "AndroidBridge")
    webView.loadUrl(url)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray100)

    ) {
        AndroidView(factory = { webView },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray100),
            update = { view ->
                val url = url
                if (view.url != url) {
                    view.loadUrl(url)
                }
            })
    }

    val androidBridge = AndroidBridge(context, webView)
//    LaunchedEffect(data) {
//        webView.evaluateJavascript("receiveDataFromApp('$data')") { result ->
//            Log.d("webView honam", result)
//        }
//    }
    Button(onClick = {
//        webView.loadUrl("javascript:receiveDataFromApp('this is honam')")
        webView.evaluateJavascript("receiveDataFromApp(${data})") { result ->
            Log.d("webview", result)
        }
    }) {
        Text("Click me")
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }
}
