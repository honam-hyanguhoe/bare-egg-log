package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.buttons.SettingSpaceBetweenButton
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.toggle.Toggle
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.molecules.swiper.CalendarSettingSwiper
import com.org.egglog.presentation.domain.community.viewmodel.PostSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.CertificateBadgeSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.CertificateBadgeViewModel
import com.org.egglog.presentation.theme.Gray200
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.Gray400
import com.org.egglog.presentation.theme.Gray500
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.theme.Warning500
import com.org.egglog.presentation.utils.ArrowRight
import com.org.egglog.presentation.utils.Sync
import com.org.egglog.presentation.utils.Upload
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BadgeScreen(
    certificateBadgeViewModel: CertificateBadgeViewModel = hiltViewModel(),
    onNavigateToSettingScreen: () -> Unit
) {
    val certificateBadgeState = certificateBadgeViewModel.collectAsState().value
    val context = LocalContext.current
    val type by certificateBadgeViewModel.type.collectAsState()

    certificateBadgeViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CertificateBadgeSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()
            CertificateBadgeSideEffect.NavigateToSettingScreen -> onNavigateToSettingScreen()
        }
    }
    val takeImageFromAlbumLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                uri?.let {
                    certificateBadgeViewModel.handleImageSelection(context, uri, type)
                }
            })

    BadgeScreen(
        isClicked = { takeImageFromAlbumLauncher.launch("image/*") },
        nurseCertificationImgUrl = certificateBadgeState.nurseCertificationImgUrl,
        hospitalCertificationImgUrl = certificateBadgeState.hospitalCertificationImgUrl,
        type = type,
        clickedText = if (type == "nurse") "다음" else "완료",
        setType = certificateBadgeViewModel::setType,
        onClickCertification = certificateBadgeViewModel::onClickCertification
    )
}

@Composable
private fun BadgeScreen(
    isClicked: () -> Unit,
    nurseCertificationImgUrl: Bitmap? = null,
    hospitalCertificationImgUrl: Bitmap? = null,
    type: String,
    clickedText: String,
    setType: (String) -> Unit,
    onClickCertification : () -> Unit
) {
    val context = LocalContext.current

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .background(NaturalWhite)
                .systemBarsPadding()
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicHeader(
                title = "인증배지 신청하기",
                hasTitle = true,
                hasArrow = true,
                hasProgressBar = true,
                indicatorValue = 0.5f,
                onClickBack = {},
            )
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(340.widthPercent(context).dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "인증 이미지 등록",
                        style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (type == "nurse") "1. 경력/간호사 면허 인증을 위한 이미지를 등록해주세요!" else "2. 병원 인증을 위한 이미지를 등록해주세요!",
                        style = Typography.displayMedium
                    )
                    Text(
                        text = if (type == "nurse ") "종류 : 간호사 면허증 원본, 간호사 면허신고 확인증 등 " else "종류 : 사원증, 재직증명서, 4대보험 가입내역 등",
                        style = Typography.displayMedium
                    )

                }
                Spacer(modifier = Modifier.height(10.dp))
                UploadButton(onClick = isClicked)
                Spacer(modifier = Modifier.height(30.dp))
                UploadImageBox(
                    nurseCertificationImgUrl = nurseCertificationImgUrl,
                    hospitalCertificationImgUrl = hospitalCertificationImgUrl,
                    type = type
                )
            }

            BigButton(
                colors = ButtonColors(
                    containerColor = Warning300,
                    contentColor = NaturalWhite,
                    disabledContainerColor = Gray300,
                    disabledContentColor = NaturalWhite
                ),
                onClick = {
                    if (type == "nurse") { setType("hospital")  } else { onClickCertification() }
                },
            ) {
                Text(
                    text = clickedText, style = Typography.bodyLarge, color = NaturalWhite
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
fun UploadImageBox(
    nurseCertificationImgUrl: Bitmap? = null,
    hospitalCertificationImgUrl: Bitmap? = null,
    type: String
) {
    val context = LocalContext.current
    val image = if (type == "nurse") nurseCertificationImgUrl else hospitalCertificationImgUrl
    Box(
        modifier = Modifier
            .width(280.widthPercent(context).dp)
            .height(420.heightPercent(context).dp)
            .border(1.dp, Gray400, shape = RoundedCornerShape(10.dp))
    ) {

        if (image != null) {
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = "인증 이미지",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun UploadButton(
    onClick: () -> Unit,
) {

    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent // 버튼 배경 투명 설정
        ), modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .height(36.dp) // 높이 설정
                    .background(
                        NaturalWhite,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                    )
                    .border(
                        1.dp,
                        Warning500,
                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                    )
                    .padding(start = 10.dp), contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "이미지 파일 업로드", color = Warning500, style = Typography.labelLarge)
            }
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .height(36.dp) // 높이 설정
                    .background(
                        Warning500, shape = RoundedCornerShape(bottomEnd = 10.dp, topEnd = 10.dp)
                    ), contentAlignment = Alignment.Center
            ) {

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Upload, modifier = Modifier.size(18.dp), color = NaturalWhite
                    )
                    Text(text = "파일 업로드", color = NaturalWhite, style = Typography.labelLarge)

                }
            }
        }
    }
}

@Preview
@Composable
fun BadgePreviewScreen() {

//    BadgeScreen()
}