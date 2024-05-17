package com.org.egglog.presentation.component.organisms.webView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.client.data.RadioButtonColorInfo
import com.org.egglog.presentation.component.atoms.buttons.RadioLabelButton
import com.org.egglog.presentation.domain.main.AndroidBridge
import com.org.egglog.presentation.domain.main.viewModel.StaticsViewModel
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RemainContentWebView(
    width: Int = 300,
    height: Int = 270,
    url: String = "https://www.egg-log.org/",
    selected: MutableState<String>?,
    data: String = "",
    type: String = "remain",
    viewModel: StaticsViewModel = hiltViewModel()
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
            if (url == "https://www.egg-log.org/remain") {
                Log.d("remainData", state.remainData)
                val script =
                    "javascript:receiveDataFromApp('${state.remainData.replace("\"", "\\\"")}')"
                webView.evaluateJavascript(script) { value ->
                    Log.d("WebView", "??? $value")
                }
            }
        }
    }

    DisposableEffect(Unit) {
        webView.addJavascriptInterface(AndroidBridge(context, webView), "AndroidBridge")
        onDispose {
            webView.removeJavascriptInterface("AndroidBridge")
            webView.destroy()
        }
    }

    LaunchedEffect(key1 = url) {
        Log.d("webview", "초기로딩")
        webView.loadUrl(url)
    }

    LaunchedEffect(key1 = state.remainData) {
        Log.d("webview", "기기 ${state.remainData}")
        val script = "javascript:receiveDataFromApp('${state.remainData.replace("\"", "\\\"")}')"
        webView.evaluateJavascript(script) { value ->
            Log.d("WebView", "돌아옴 기기 $value")
        }
    }




    if (type == "remain") {
        val radioList = arrayListOf("Week", "Month")
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp)) {
            radioList.mapIndexed { index, text ->
                if (selected != null) {
                    RadioLabelButton(
                        text = text,
                        isSelected = selected.value == text,
                        onClick = { clickedIdx ->
                            selected.value = clickedIdx
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
                }
                if (index != radioList.size - 1) Spacer(
                    modifier = Modifier.padding(3.widthPercent(context).dp)
                )
            }
        }
    }

    Card(
        modifier = Modifier
            .height(height.heightPercent(context).dp)
            .fillMaxWidth()
            .background(color = Gray100, shape = RoundedCornerShape(20.dp)),
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

class NoScrollWebView(context: Context) : WebView(context) {
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE)
            return false
        return super.onTouchEvent(event)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, l, t)
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
    }
}

//            webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    super.onPageFinished(view, url)
//                    val initialScript = "javascript:receiveDataFromApp('${state.remainData.replace("\"", "\\\"")}')"
//                    evaluateJavascript(initialScript) { value ->
//                        Log.d("WebView", "초기 데이터 전송 $value")
//                    }
//                }
//            }

//    LaunchedEffect(key1 = state.remainData) {
//        if (state.remainData.isNotEmpty()) {
//            Log.d("WebView", "보이냐고 왜 안보이냐고")
//            val script = "javascript:receiveDataFromApp('${state.remainData.replace("\"", "\\\"")}')"
//            webView.evaluateJavascript(script) { value ->
//                Log.d("WebView", "JavaScript response: $value")
//            }
//        }
//    }