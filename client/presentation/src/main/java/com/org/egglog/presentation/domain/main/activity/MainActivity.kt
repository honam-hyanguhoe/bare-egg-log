package com.org.egglog.presentation.domain.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.org.egglog.presentation.domain.community.posteditor.activity.PostEditorActivity
import com.org.egglog.presentation.domain.main.screen.MainScreen
import com.org.egglog.presentation.domain.main.viewModel.MainViewModel
import com.org.egglog.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientTheme {
                MainScreen()
            }
        }
    }
}