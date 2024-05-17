package com.org.egglog.presentation.domain.community.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.org.egglog.presentation.domain.community.navigation.CommunityNavigationHost
import com.org.egglog.presentation.domain.community.screen.PostListScreen
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            ClientTheme {
                CommunityNavigationHost()
            }
        }
    }
}