package com.org.egglog.presentation.domain.community.posteditor.screen

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.domain.community.posteditor.viewmodel.WritePostViewModel
import com.org.egglog.presentation.theme.Gray200
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.utils.Add
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ImageUploader(
    viewModel: WritePostViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    val takeImageFromAlbumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.handleImageSelection(context, uri)
            }
        }
    )

    ImageUploader(
        isClicked = { takeImageFromAlbumLauncher.launch("image/*") },
        uploadedImages = state.uploadImages
    )
}


@Composable
private fun ImageUploader(
    isClicked: () -> Unit,
    uploadedImages: List<Bitmap> = listOf()
) {

    Row {
        ImageUploadButton(
            isClicked = { isClicked() }
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(uploadedImages) { imageBitmap ->
                ImageUploadBox(imageBitmap = imageBitmap)
            }
        }
    }
}

@Composable
fun ImageUploadButton(
    isClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(70.widthPercent(context).dp)
            .clickable(
                enabled = true,
                onClickLabel = "업로드",
                onClick = { isClicked() }
            )
            .background(color = Gray200, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Add,
            modifier = Modifier.size(30.widthPercent(context).dp),
            color = NaturalBlack
        )
    }
}

@Composable
fun ImageUploadBox(
    imageBitmap: Bitmap? = null
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(70.widthPercent(context).dp)
            .border(1.dp, NaturalBlack, shape = RoundedCornerShape(10.dp))
    ) {
        if (imageBitmap != null) {
            imageBitmap?.asImageBitmap()?.let { imgBitmap ->
                Image(
                    bitmap = imgBitmap,
                    contentDescription = "Uploaded Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

