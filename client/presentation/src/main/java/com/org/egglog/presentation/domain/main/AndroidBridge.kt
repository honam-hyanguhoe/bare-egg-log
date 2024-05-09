package com.org.egglog.presentation.domain.main

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast

class AndroidBridge(private val context: Context, private val webView: WebView) {
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    fun sendDataToReact(data: String) {
        webView.evaluateJavascript("receiveDataFromApp('$data')") { result ->
            Log.d("webView", result)
        }
    }
}