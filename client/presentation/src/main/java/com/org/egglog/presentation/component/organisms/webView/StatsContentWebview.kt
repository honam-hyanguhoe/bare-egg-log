package com.org.egglog.presentation.component.organisms.webView

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.org.egglog.presentation.domain.main.AndroidBridge
import com.org.egglog.presentation.domain.main.viewModel.StaticsViewModel
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun StatsContentWebView(
    width: Int = 300,
    height: Int = 270,
    url: String = "https://www.egg-log.org/stats",
    selected: MutableState<String>?,
    data : String = "",
    type : String = "Stats",
    viewModel : StaticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value

    val webView = remember {
        NoScrollWebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                allowContentAccess = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                useWideViewPort = true
                loadWithOverviewMode = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                setSupportZoom(false)
                displayZoomControls = false
                builtInZoomControls = false
            }
        }
    }

    webView.setBackgroundColor(Color(0xFFF2F4F7).toArgb())
    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (url == "https://www.egg-log.org/statics") {
                Log.d("web-stats", "버튼 기기 ${state.statsData}")
                val script = "javascript:receiveStatsFromApp('${state.statsData.replace("\"", "\\\"")}')"
                webView.evaluateJavascript(script) { value ->
                    Log.d("web-stats",  "초기 로딩 statics $value")
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }

    LaunchedEffect(key1 = url) {
       Log.d("web-stats", "초기로딩")
        webView.loadUrl(url)
    }

    LaunchedEffect(key1 = state.statsData) {
        Log.d("web-stats", "기기 ${state.statsData}")
        val script = "javascript:receiveStatsFromApp('${state.statsData.replace("\"", "\\\"")}')"
        webView.evaluateJavascript(script) { value ->
            Log.d("web-stats", "stats 돌아옴 기기 $value")
        }
    }

    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(width.widthPercent(context).dp)
            .background(color = Gray100, shape = RoundedCornerShape(20.dp))
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier
                .height(height.heightPercent(context).dp)
                .width(width.widthPercent(context).dp)
                .background(Gray100)
        )
    }
}
