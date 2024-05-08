package com.org.egglog.presentation.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log

fun getVersionInfo(context: Context?): String {
    var version = "Unknown"
    val packageInfo: PackageInfo
    if (context == null) {
        return version
    }
    try {
        packageInfo = context.applicationContext
            .packageManager
            .getPackageInfo(context.applicationContext.packageName, 0)
        version = packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("getVersionInfo :", "${e.message}")
    }
    return version
}