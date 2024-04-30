package com.org.egglog.component.organisms.webView

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.org.egglog.utils.heightPercent
import com.org.egglog.theme.NaturalBlack

@Composable
fun FullPageWebView(
    height : Int = 250,
    url : String = "https://www.egg-log.org/"
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

            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }

    AndroidView(
        factory = { webView },
        modifier = Modifier
            .fillMaxSize()
            .height(height.heightPercent(context).dp)
            .border(1.dp, NaturalBlack),
        update = { view ->
            if (view.url != url) view.loadUrl(url)
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }
}
