package com.org.egglog.presentation.component.organisms.webView

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.org.egglog.presentation.domain.main.AndroidBridge
import com.org.egglog.presentation.theme.Gray100


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


    Button(onClick = {

        val script =
            ("javascript:receiveDataFromApp('" + data.replace("\"", "\\\"")).toString() + "')"
        Log.d("webview", "script --- $script")
        webView.evaluateJavascript(
            script
        ) { value -> Log.d("WebView", "JavaScript response: $value") }

    }) {
        Text("Click me")
    }

//    LaunchedEffect(data) {
//        Log.d("webview", "데이터 변경 감지")
//        val script =
//            "javascript:receiveDataFromApp('${data.replace("\"", "\\\"")}')" // Improved string template
//        webView.evaluateJavascript(script) { value ->
//            Log.d("WebView", "JavaScript response: $value")
//        }
//    }

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }
}
