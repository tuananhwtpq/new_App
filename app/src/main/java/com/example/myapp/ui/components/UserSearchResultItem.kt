package com.example.myapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.ProfileImage
import com.example.myapp.data.model.Social
import com.example.myapp.data.model.User
import com.example.myapp.data.model.UserWithPhoto
import com.example.myapp.ui.theme.MyAppTheme


@Composable
fun UserSearchResultItem(
    userWithPhoto: UserWithPhoto,
    onUserClick: (String) -> Unit,
    placeholder: Painter? = null,
    onPhotoClick: (String) -> Unit
) {

    val placeholderPainter = remember { ColorPainter(Color.LightGray) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onUserClick(userWithPhoto.user.username.toString())
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = userWithPhoto.user.profile_image?.medium,
                contentDescription = "User avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(
                    text = userWithPhoto.user.name.toString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@" + userWithPhoto.user.username.toString(),
                    color = Color.Gray
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            userWithPhoto.photos.take(3).forEach { photo ->

                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(photo.urls?.small)
                    .build()

                AsyncImage(
                    model = imageRequest,
                    contentDescription = photo.description,
                    contentScale = ContentScale.Crop,
                    placeholder = placeholderPainter,
                    error = placeholderPainter,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onPhotoClick(photo.id)
                        }
                )
            }
        }
    }
}


@Preview
@Composable
fun UserSearchResultItemPreview() {

    val listUser = listOf<User>(
        User(
            id = "1", username = "tuan anh", name = "tuan anh",
            profile_image = ProfileImage(
                small = "",
                medium = "",
                large = ""
            ),
            first_name = "",
            last_name = "",
            twitter_username = "",
            portfolio_url = "",
            bio = "",
            location = "",
            instagram_username = "",
            total_collections = 15,
            total_likes = 15,
            total_photos = 18,
            downloads = 18,
            social = Social(
                instagram_username = "",
                portfolio_url = "",
                twitter_username = ""
            ),
        )
    )

    val images = listOf<PhotoResponse>()

//    val images = listOf<PhotoResponse>(
//        PhotoResponse(
//            id = 1 ,
//            description = "" ,
//            urls = PhotoUrl(
//                raw = "",
//                full = "",
//                regular = "",
//                small = "",
//                thumb = ""
//            ),
//            user = ,
//            blur_hash = ,
//            downloads = ,
//            likes = ,
//            exif = ,
//            color = ,
//            tags = ,
//            width = ,
//            height =
//        )
//    )

    MyAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            UserSearchResultItem(
                userWithPhoto = UserWithPhoto(
                    user = TODO(),
                    photos = TODO()
                ),

                onUserClick = {},
                placeholder = null,
                onPhotoClick = {}
            )
        }
    }
}