package com.org.egglog.presentation.component.organisms.webView

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.ContentView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.org.egglog.client.data.RadioButtonColorInfo
import com.org.egglog.presentation.component.atoms.buttons.RadioLabelButton
import com.org.egglog.presentation.component.molecules.radioButtons.DayRadioButton
import com.org.egglog.presentation.domain.main.AndroidBridge
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.utils.widthPercent
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ContentWebView(
    width: Int = 300,
    height: Int = 250,
    url: String = "https://www.egg-log.org/",
    data: String = "",
    selected: MutableState<String>
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
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
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            webViewClient = WebViewClient()
        }
    }

    // JavaScript 인터페이스 추가
    DisposableEffect(Unit) {
        webView.addJavascriptInterface(AndroidBridge(context, webView), "AndroidBridge")
        onDispose {
            webView.removeJavascriptInterface("AndroidBridge")
            webView.destroy()
        }
    }

    // URL 로드 처리
    LaunchedEffect(key1 = url) {
        webView.loadUrl(url)
    }

    // JavaScript 실행용 라디오 버튼
    val radioList = arrayListOf("Week", "Month")
    Row(Modifier.fillMaxWidth()) {
        radioList.mapIndexed { index, text ->
            RadioLabelButton(
                text = text,
                isSelected = selected.value == text,
                onClick = { clickedIdx ->
                    selected.value = clickedIdx
                    val script = "javascript:receiveDataFromApp('${data.replace("\"", "\\\"")}')"
                    webView.evaluateJavascript(script) { value ->
                        Log.d("WebView", "JavaScript response: $value")
                    }
                },
                radioButtonColorInfo = RadioButtonColorInfo(
                    selectedBorderColor = NaturalBlack,
                    selectedContainerColor = NaturalBlack,
                    selectedTextColor = Gray100,
                    unSelectedBorderColor = NaturalBlack,
                    unSelectedContainerColor = Gray100,
                    unSelectedTextColor = NaturalBlack
                ),
                textStyle = Typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                width = 64,
                padding = 8
            )
            if (index != radioList.size - 1) Spacer(
                modifier = Modifier.padding(
                    3.widthPercent(context).dp
                )
            )
        }
    }

    // WebView 표시
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray100)
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray100)
        )
    }

    // JavaScript 실행 버튼
//    Button(onClick = {
//        val script = "javascript:receiveDataFromApp('${data.replace("\"", "\\\"")}')"
//        webView.evaluateJavascript(script) { value ->
//            Log.d("WebView", "JavaScript response: $value")
//        }
//    }) {
//        Text("Click me")
//    }
}