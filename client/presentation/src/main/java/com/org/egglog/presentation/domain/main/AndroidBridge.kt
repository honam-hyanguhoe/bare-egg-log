package com.org.egglog.presentation.domain.main

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class AndroidBridge (private val context: Context){
    @JavascriptInterface
    fun showToast(toast:String){
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun sendDataToReact(data: String) {

    }
}