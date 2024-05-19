package com.org.egglog.presentation.domain.main.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.org.egglog.presentation.domain.main.navigation.MainNavigationHost
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLinkUri: Uri? = intent?.data
        Log.d("deep", "main ${deepLinkUri.toString()}")
        setContent {
            ClientTheme {
                MainNavigationHost(deepLinkUri)
            }
        }

        // 권한 요청 예시
        askForPermissions(listOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.SCHEDULE_EXACT_ALARM
        ))
    }


    private fun askForPermissions(permissionList: List<String>) {
        val requestList = ArrayList<String>()
        for (permission in permissionList) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestList.add(permission)
            }
        }

        if (requestList.isNotEmpty()) {
            for(permission in requestList) requestPermissionLauncher.launch(permission)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.e("Permission: ", "Granted")
            } else {
                Log.e("Permission: ", "Denied")
            }
        }
}