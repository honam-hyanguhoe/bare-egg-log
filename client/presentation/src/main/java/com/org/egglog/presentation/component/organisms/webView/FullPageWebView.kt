package com.org.egglog.presentation.component.organisms.webView

import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun FullPageWebView(
    height: Float = 250f,
    width: Float = 300f,
    url: String = "https://www.egg-log.org/",
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.apply {
                allowContentAccess = true
                javaScriptEnabled= true
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

    Box(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .background(color = NaturalWhite)
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.matchParentSize(),
            update = { view ->
                if (view.url != url) view.loadUrl(url)
            }
        )
        Button(
            onClick = {
                onClose()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("닫기")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("webview", "webview Destory")
            webView.destroy()
        }
    }
}
