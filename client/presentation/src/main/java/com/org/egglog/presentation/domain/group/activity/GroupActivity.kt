package com.org.egglog.presentation.domain.group.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.org.egglog.presentation.domain.community.navigation.CommunityNavigationHost
import com.org.egglog.presentation.domain.group.navigation.GroupNavigationHost
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ClientTheme {
                GroupNavigationHost()
            }
        }
    }
}