package com.org.egglog.presentation.domain.community.posteditor.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.org.egglog.presentation.domain.community.posteditor.navigation.PostNavigationHost
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostEditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            ClientTheme {
                PostNavigationHost{
                    Log.d("커뮤니티", "close 눌러써")
                    finish()
                }
            }
        }
    }
}