package com.example.myapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import io.mortezanedaei.blurhash.compose.AsyncBlurImage

@OptIn(ExperimentalCoilApi::class)
@Composable
fun BlurHashPlaceholder(
    blurHash: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Blurred placeholder"
) {
    if (!blurHash.isNullOrBlank()) {
//        BlurHash(
//            blurHash = blurHash,
//            contentDescription = contentDescription,
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )

        AsyncBlurImage(
            imageUrl = blurHash,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}