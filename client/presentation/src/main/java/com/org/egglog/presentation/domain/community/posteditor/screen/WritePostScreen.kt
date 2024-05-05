package com.org.egglog.presentation.domain.community.posteditor.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.inputs.MultiInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.community.posteditor.viewmodel.WritePostViewModel
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Gray25
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.heightPercent
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun WritePostScreen(
    viewModel: WritePostViewModel = hiltViewModel(),
    onCloseClick: () -> Unit
) {
    val state = viewModel.collectAsState().value
    Log.d("커뮤니티", "writePostScreen 업로드 이미지 확인${state.uploadImages} ${state.title} ${state.content}")
    WritePostScreen(
        title = state.title,
        content = state.content,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onCloseClick = onCloseClick,
        onPostClick = viewModel::onPostClick,
        viewModel = viewModel
    )
}

@Composable
private fun WritePostScreen(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onPostClick: () -> Unit,
    viewModel : WritePostViewModel
) {
    val focusManager = LocalFocusManager.current
    Surface {
        BoxWithConstraints {
            val screenHeight = maxHeight
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = NaturalWhite)
                    .padding(bottom = 24.heightPercent(LocalContext.current).dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicHeader(
                        hasLeftClose = true,
                        onClickClose = onCloseClick,
                    )
                    Column(
                        modifier = Modifier.padding(top = 12.heightPercent(LocalContext.current).dp)
                    ) {
                        Text(text = "제목", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
                        SingleInput(
                            modifier = Modifier,
                            text = title,
                            placeholder = "제목을 입력해주세요",
                            onValueChange = onTitleChange,
                            focusManager = focusManager
                        )
                        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))
                        Text(text = "내용", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
                        MultiInput(
                            text = content,
                            placeholder = "내용을 입력해주세요",
                            onValueChange = onContentChange,
                            focusManager = focusManager,
                            height = 150
                        )
                        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))
                        Text(text = "사진(선택)", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp).width(20.dp))
                        ImageUploader(viewModel = viewModel)
                    }
                }
                BigButton(
                    colors = ButtonColors(
                        contentColor = NaturalWhite,
                        containerColor = Warning300,
                        disabledContainerColor = Gray25,
                        disabledContentColor = Gray300
                    ), onClick = onPostClick
                ) {
                    Text(
                        style = Typography.bodyLarge,
                        text = "작성완료"
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun WritePostPreviewScreen() {
    ClientTheme {
        WritePostScreen(
            title = "",
            content = "",
            onTitleChange = {},
            onContentChange = {},
            onCloseClick = {},
            onPostClick = {},
            viewModel = hiltViewModel()
        )
    }
}