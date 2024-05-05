package com.org.egglog.presentation.domain.main.screen

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.org.egglog.presentation.domain.community.posteditor.activity.PostEditorActivity
import com.org.egglog.presentation.domain.main.viewModel.MainViewModel
import com.org.egglog.presentation.theme.NaturalWhite
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
) {
    val context = LocalContext.current
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NaturalWhite)
        ) {
            Text(text = "main")
            Button(onClick = {
                Log.d("main", "move to postEditor")
                context.startActivity(
                    Intent(
                        context,
                        PostEditorActivity::class.java
                    )
                )
            }) {
                Text("글쓰기 페이지로 이동")
            }
        }
    }

}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}