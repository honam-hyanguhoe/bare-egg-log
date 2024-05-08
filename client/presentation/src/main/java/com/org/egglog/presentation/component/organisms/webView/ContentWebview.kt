package com.org.egglog.presentation.component.organisms.webView

import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.org.egglog.presentation.domain.main.AndroidBridge
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import java.nio.charset.StandardCharsets

@Composable
fun ContentWebView(
    width: Int = 300, height: Int = 250, url: String = "https://www.egg-log.org/"
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            // WebView settings
            settings.apply {
                allowContentAccess = true
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                useWideViewPort = true
                loadWithOverviewMode = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            // 하드웨어 가속 사용 설정
            setLayerType(
                View.LAYER_TYPE_HARDWARE, null
            )

//            webViewClient = WebViewClient()


            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.evaluateJavascript("javascript:receiveDataFromApp('this is honam')") { result ->
                        Log.d("webView", "Result from JS: $result")
                    }
                }
            }


            loadUrl(url)

        }
    }

    webView.addJavascriptInterface(AndroidBridge(context, webView), "AndroidBridge")

    Card(
        modifier = Modifier
            .fillMaxSize()
//            .size(
//                width = width.widthPercent(context).dp,
//                height = height.heightPercent(context).dp
//            )
            .border(0.5.dp, Gray100), shape = RoundedCornerShape(20.dp)
    ) {
        AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize(), update = { view ->
            val url = url
            if (view.url != url) {
                view.loadUrl(url)
            }
        })
//        Button(onClick = {
//
//            webView.evaluateJavascript("mobileToJavascript()") { }
//
//        }) {
//
//            Text(text = "안녕 호남아")
//        }
    }



    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }
}
