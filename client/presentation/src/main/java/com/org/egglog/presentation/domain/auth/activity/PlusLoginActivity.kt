package com.org.egglog.presentation.domain.auth.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.org.egglog.presentation.domain.auth.navigation.PlusLoginNavigationHost
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlusLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientTheme {
                PlusLoginNavigationHost()
            }
        }
    }
}