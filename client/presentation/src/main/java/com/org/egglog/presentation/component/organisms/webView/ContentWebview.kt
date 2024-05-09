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

    // radio
    val radioList = arrayListOf("Week", "Month")

    Row(Modifier.fillMaxWidth()) {
        radioList.mapIndexed { index, text ->
            RadioLabelButton(
                text = text,
                isSelected = selected.value == text,
                onClick = { clickedIdx ->
                    Log.d("", clickedIdx)
                    selected.value = clickedIdx

                    Log.d("webview", "data change $data")
//                    val tempData = "[{\"color\":\"#9B8AFB\",\"name\":\"OFF\",\"value\":3},{\"color\":\"#18C5B5\",\"name\":\"DAY\",\"value\":1}]"
//                    val script1 =
//                        ("javascript:receiveDataFromApp('" + tempData.replace("\"", "\\\"")).toString() + "')"
//                    Log.d("webview", "script --- $script1")
//                    webView.evaluateJavascript(
//                        script1
//                    ) { value -> Log.d("WebView", "JavaScript response: $value") }
                    updateWebView(webView, data)
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
                    3.widthPercent(
                        context
                    ).dp
                )
            )
        }
    }



    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray100)

    ) {
        Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)) {

        }
        AndroidView(factory = { webView },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray100),
            update = { view ->
                val url = url
                if (view.url != url) {
                    view.loadUrl(url)
                }
            }
        )
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

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
        }
    }
}

private fun updateWebView(webView: WebView, data: String) {
    val script =
        "javascript:receiveDataFromApp('${data.replace("\"", "\\\"")}')" // Improved string template
    webView.evaluateJavascript(script) { value ->
        Log.d("WebView", "JavaScript response: $value")
    }
}