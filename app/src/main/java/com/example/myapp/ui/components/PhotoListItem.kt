package com.example.myapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.PhotoUrl
import com.example.myapp.data.model.ProfileImage
import com.example.myapp.data.model.User
import com.example.myapp.ui.theme.MyAppTheme
import com.wajahatiqbal.blurhash.BlurHashPainter


@Composable
fun PhotoListItem(
    photo: PhotoResponse,
    onItemClick: (String) -> Unit,
    placeholder: Painter? = null,
    onUserClick: (String) -> Unit
) {


    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    photo.user?.username.let { onUserClick(it.toString()) }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = photo.user?.profile_image?.medium,
                contentDescription = "User avatar",
                placeholder = placeholder,
                error = placeholder,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = photo.user?.name ?: "Unknown User",
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable() {
                    onItemClick(photo.id)
                },
            shape = RoundedCornerShape(16.dp),

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(photo.width.toFloat() / photo.height.toFloat())
            ) {

                val placeholderColor = remember(photo.color) {
                    try {
                        Color(android.graphics.Color.parseColor(photo.color))
                    } catch (e: Exception) {
                        Color.LightGray
                    }
                }

                val placeholderPainter = ColorPainter(placeholderColor)

//                AsyncImage(
//                    model = photo.urls?.regular,
//                    contentDescription = photo.description ?: "Unknown Photo",
//                    modifier = Modifier.fillMaxSize(),
//                    placeholder = placeholderPainter,
//                    error = placeholderPainter,
//                    contentScale = ContentScale.Crop
//                )

                AsyncImage(
                    model = photo.urls?.regular,
                    contentDescription = photo.description ?: "Unknown Photo",
                    modifier = Modifier.fillMaxSize(),
                    placeholder = BlurHashPainter(
                        blurHash = photo.blur_hash,
                        width = photo.width,
                        height = photo.height,
                        punch = 1F,
                        scale = 0.1F
                    ),
                    error = BlurHashPainter(
                        blurHash = photo.blur_hash,
                        width = photo.width,
                        height = photo.height,
                        punch = 1F,
                        scale = 0.1F
                    ),
                    contentScale = ContentScale.Crop
                )

            }
        }

    }
}


@Preview
@Composable
fun PhotoListItemPreview() {

    val newUser = User(
        id = "2",
        name = "Tuan Anh",
        username = "Tuan Anh",
        profile_image = ProfileImage(
            small = null,
            medium = "https://res.cloudinary.com/dguxc4au3/image/upload/v1754787853/78b303974d8b705c370d57a9f9bd48ca_gwxnbs.jpg",
            large = null
        ),
        first_name = null,
        last_name = null,
        twitter_username = null,
        portfolio_url = null,
        bio = null,
        location = null,
        instagram_username = null,
        total_collections = null,
        total_likes = null,
        total_photos = null,
        downloads = null,
        social = null
    )


    val fakePhoto = PhotoResponse(
        id = "2",
        description = null,
        urls = PhotoUrl(
            raw = "",
            full = "",
            regular = "https://res.cloudinary.com/dguxc4au3/image/upload/v1754787853/78b303974d8b705c370d57a9f9bd48ca_gwxnbs.jpg",
            small = "",
            thumb = null,
            small_s3 = null
        ),
        user = newUser,
        downloads = 0,
        likes = 0,
        exif = null,
        tags = null,
        width = 3000,
        height = 6000,
        blur_hash = "123456",
        color = "111"
    )

    MyAppTheme {

        Surface(
            modifier = Modifier.fillMaxWidth(),
        ) {

            val imageHolder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)

            PhotoListItem(
                photo = fakePhoto,
                onItemClick = {},
                placeholder = imageHolder,
                onUserClick = {}
            )
        }

    }
}
