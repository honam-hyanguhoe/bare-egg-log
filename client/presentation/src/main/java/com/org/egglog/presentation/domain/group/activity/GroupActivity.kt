package com.org.egglog.presentation.domain.group.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.org.egglog.presentation.domain.community.navigation.CommunityNavigationHost
import com.org.egglog.presentation.domain.group.navigation.GroupNavigationHost
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val deepLinkUri: Uri? = intent?.data

        Log.d("deep", "group Activity ${deepLinkUri.toString()}")

        val code = intent?.getStringExtra("code")
        val name = intent?.getStringExtra("name")
        val groupId = intent?.getStringExtra("groupId")

        setContent{
            ClientTheme {
                GroupNavigationHost(deepLinkUri = deepLinkUri, code = code ?: "", groupName = name ?: "", groupId = groupId?.toLong() ?: 0)
            }
        }
    }
}