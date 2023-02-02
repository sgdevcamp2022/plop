package com.plop.plopmessenger.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.plop.plopmessenger.R
import com.plop.plopmessenger.presentation.theme.Green100

object ProfileImageValue {
    const val ChatTopBarImageSize = 36

    const val ChatTitleImageSize = 104

    val ProfileWithStateSize = 56.dp
    val StateSize = 16.dp
}

@Composable
fun ProfileImageWithState(
    imageURL: String,
    profileSize: Dp = ProfileImageValue.ProfileWithStateSize,
    isActivate: Boolean = false
) {
    Box() {
        ProfileImage(imageURL = imageURL, modifier = Modifier.size(profileSize))
        if(isActivate) {
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(ProfileImageValue.StateSize)
                    .border(2.dp, color = MaterialTheme.colors.background, shape = CircleShape),
                color = Green100
            ) {

            }
        }
    }
}

@Composable
fun ProfileImages(
    images: List<String?>,
    modifier: Modifier = Modifier,
    profileSize: Int = ProfileImageValue.ChatTopBarImageSize
) {
    if(images.size == 1) ProfileImage(imageURL = images.first(), modifier = modifier, profileSize = profileSize)
    else if(images.size == 2) DoubleProfileImage(images = images, modifier = modifier, profileSize = profileSize)
    else TripleProfileImage(images = images, modifier = modifier, profileSize = profileSize)
}

@Composable
fun DoubleProfileImage(
    images: List<String?>,
    modifier: Modifier = Modifier,
    profileSize: Int = ProfileImageValue.ChatTopBarImageSize
) {
    val widthValue = (profileSize / 1.75).toInt().dp

    Box(
        modifier = Modifier.width(profileSize.dp)
    ) {
        ProfileImage(images[0]?: "", modifier = modifier
            .size(widthValue)
            .align(Alignment.CenterStart))
        ProfileImage(images[1]?: "", modifier = modifier
            .size(widthValue)
            .align(Alignment.CenterEnd))
    }
}

@Composable
fun TripleProfileImage(
    images: List<String?>,
    modifier: Modifier = Modifier,
    profileSize: Int = ProfileImageValue.ChatTopBarImageSize
) {
    val widthValue = (profileSize / 1.75).toInt().dp

    Box(
        modifier = Modifier.size(profileSize.dp)
    ) {
        ProfileImage(images[0]?: "", modifier = modifier
            .size(widthValue)
            .align(Alignment.TopStart))
        ProfileImage(images[1]?: "", modifier = modifier
            .size(widthValue)
            .align(Alignment.TopEnd))
        ProfileImage(images[2]?: "", modifier = modifier
            .size(widthValue)
            .align(Alignment.BottomCenter))
    }
}


@Composable
fun ProfileImage(
    imageURL: String?,
    modifier: Modifier = Modifier,
    profileSize: Int = ProfileImageValue.ChatTopBarImageSize
) {
    if(imageURL.isNullOrBlank()) BlankProfileImage(modifier = modifier.size(profileSize.dp))
    else NonBlankProfileImage(imageURL = imageURL, modifier = modifier.size(profileSize.dp))
}


@Composable
fun NonBlankProfileImage(
    imageURL: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageURL)
                .crossfade(true)
                .build(),
            contentDescription = "profile Image",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.blank_profile)
        )
    }
}

@Composable
fun BlankProfileImage(
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.blank_profile),
            contentDescription = "blank profile image",
            contentScale = ContentScale.Crop)
    }
}