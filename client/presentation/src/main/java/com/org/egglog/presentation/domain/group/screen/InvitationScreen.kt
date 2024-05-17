package com.org.egglog.presentation.domain.group.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.atoms.inputs.PassInput
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailSideEffect
import com.org.egglog.presentation.domain.group.viewmodel.GroupDetailViewModel
import com.org.egglog.presentation.domain.group.viewmodel.InviteMemberSideEffect
import com.org.egglog.presentation.domain.group.viewmodel.InviteMemberViewModel
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun InvitationScreen(
    inviteMemberViewModel: InviteMemberViewModel = hiltViewModel(),
    code: String,
    groupName: String,
    onNavigateToGroupListScreen: () -> Unit,
) {
    val context = LocalContext.current
    val inviteMemberState = inviteMemberViewModel.collectAsState().value

    LaunchedEffect(code) {
        inviteMemberViewModel.setInvitationCode(code)
    }


    inviteMemberViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is InviteMemberSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    InvitationScreen(
        code = code,
        groupName = groupName,
        groupPassword = inviteMemberState.invitePassword,
        onGroupPasswordChange = inviteMemberViewModel::onChangeInvitePassword,
        onJoinGroup = inviteMemberViewModel::onJoinGroup,
        onNavigateToGroupListScreen = onNavigateToGroupListScreen
    )
}

@Composable
private fun InvitationScreen(
    code: String,
    groupName: String,
    groupPassword: String = "",
    onGroupPasswordChange: (String) -> Unit,
    onJoinGroup : () -> Unit,
    onNavigateToGroupListScreen: () -> Unit,
    ) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NaturalWhite)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            LocalImageLoader(
                imageUrl = R.drawable.big_fry,
                modifier = Modifier.size(450.widthPercent(context).dp)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(340.widthPercent(context).dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Column(
                        modifier = Modifier
                            .width(320.widthPercent(context).dp)
                            .padding(top = 100.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "${groupName} 모여라에",
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "참여하시겠습니까",
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            "비밀번호를 입력해주세요",
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    PassInput(pin = groupPassword, onValueChange = onGroupPasswordChange)
                    Spacer(modifier = Modifier.height(70.dp))
                }
                BigButton(colors = ButtonColors(
                    containerColor = Warning300,
                    contentColor = NaturalWhite,
                    disabledContainerColor = Gray300,
                    disabledContentColor = NaturalWhite
                ), onClick = {
                    onJoinGroup()
                    onNavigateToGroupListScreen()
                }) {
                    Text(text = "그룹 참여하기", color = NaturalWhite)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
fun InvitationPreviewScreen() {
    InvitationScreen(code = "1234", groupName = "호남향우회", onNavigateToGroupListScreen = {})
}