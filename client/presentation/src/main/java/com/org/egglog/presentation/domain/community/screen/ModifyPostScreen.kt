package com.org.egglog.presentation.domain.community.screen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.inputs.MultiInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.community.viewmodel.ModifyPostSideEffect
import com.org.egglog.presentation.domain.community.viewmodel.ModifyPostViewModel
import com.org.egglog.presentation.domain.community.viewmodel.WritePostViewModel
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun ModifyPostScreen(
    viewModel: ModifyPostViewModel = hiltViewModel(),
    postId: Int,
    onNavigateToDetailScreen: (Int) -> Unit
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ModifyPostSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    ModifyPostScreen(
        postId = postId,
        title = state.title,
        content = state.content,
        onTitleChange = viewModel::onChangeTitle,
        onContentChange = viewModel::onChangeContent,
        onPostClick = viewModel::onClickPost,
        onNavigateToDetailScreen = onNavigateToDetailScreen
    )
}

@Composable
private fun ModifyPostScreen(
    postId: Int,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onPostClick: () -> Unit,
    onNavigateToDetailScreen: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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
                        onClickClose = { (context as? Activity)?.onBackPressed() },
                    )
                    Column(
                        modifier = Modifier.padding(top = 12.heightPercent(LocalContext.current).dp)
                    ) {
                        Text(text = "제목", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
                        SingleInput(
                            modifier = Modifier.width(320.widthPercent(LocalContext.current).dp),
                            text = title,
                            placeholder = "제목을 입력해주세요",
                            onValueChange = onTitleChange,
                            focusManager = focusManager
                        )
                        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))
                        Text(text = "내용", style = Typography.displayLarge)
                        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
                        MultiInput(
                            modifier = Modifier.width(320.widthPercent(LocalContext.current).dp),
                            text = content,
                            placeholder = "내용을 입력해주세요",
                            onValueChange = onContentChange,
                            focusManager = focusManager,
                            height = 150
                        )
                    }
                }
                BigButton(
                    colors = ButtonColors(
                        contentColor = NaturalWhite,
                        containerColor = Warning300,
                        disabledContainerColor = Gray300,
                        disabledContentColor = NaturalWhite
                    ), onClick = {
                        onPostClick()
                        onNavigateToDetailScreen(postId)
                    },
                    enabled = (title != "" && content != "") // title과 content 값이 없으면 disabled
                ) {
                    Text(
                        style = Typography.bodyLarge,
                        color = NaturalWhite,
                        text = "수정 완료"
                    )
                }
            }
        }
    }
}