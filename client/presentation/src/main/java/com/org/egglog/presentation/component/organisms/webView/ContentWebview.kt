package com.org.egglog.presentation.component.organisms.webView

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.ContentView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.org.egglog.presentation.theme.Indigo400
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ContentWebView(
    width: Int = 300,
    height: Int = 270,
    url: String = "https://www.egg-log.org/",
    data: String = "",
    selected: MutableState<String>?,
    type : String = "remain"
) {
    val context = LocalContext.current
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
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            webViewClient = WebViewClient()
            loadUrl(url)
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
        Log.d("webView", "웹뷰 띄울게요")
        webView.loadUrl(url)
    }

    if(type == "remain"){
        val radioList = arrayListOf("Week", "Month")
        Row(Modifier.fillMaxWidth().padding(top = 10.dp, start=10.dp)) {
            radioList.mapIndexed { index, text ->
                if (selected != null) {
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
                }
                if (index != radioList.size - 1) Spacer(
                    modifier = Modifier.padding(
                        3.widthPercent(context).dp
                    )
                )
            }
        }


    }


    Card(
        modifier = Modifier
//            .fillMaxSize()
//            .height(height.heightPercent(context).dp)
            .fillMaxHeight()
            .width(width.widthPercent(context).dp)
            .background(color = Warning300)
//            .background(color = Gray100)
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier
                .height(height.heightPercent(context).dp)
                .width(width.widthPercent(context).dp)
//                .fillMaxSize()
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
}
