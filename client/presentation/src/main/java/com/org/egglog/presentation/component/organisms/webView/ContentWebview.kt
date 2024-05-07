package com.org.egglog.presentation.component.organisms.webView

import android.os.Build
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
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.Gray200
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.theme.NaturalBlack

@Composable
fun ContentWebView(
     width : Int = 300,
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

            // 하드웨어 가속 사용 설정
            setLayerType(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    View.LAYER_TYPE_HARDWARE
                else
                    View.LAYER_TYPE_SOFTWARE,
                null
            )

            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }

//    AndroidView(
//        factory = { webView },
//        modifier = Modifier
//            .width(width.widthPercent(context).dp)
//            .height(height.heightPercent(context).dp)
//        ,
//        update = { view ->
//            if (view.url != url) view.loadUrl(url)
//        }
//    )

    Card(
        modifier = Modifier
            .size(
                width = width.widthPercent(context).dp,
                height = height.heightPercent(context).dp
            ),
        shape = RoundedCornerShape(20.dp) // 모서리 둥글게 20dp
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                val url = url
                if (view.url != url) {
                    view.loadUrl(url)
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }
}
