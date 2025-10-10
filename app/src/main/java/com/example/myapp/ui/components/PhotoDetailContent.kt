package com.example.myapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapp.R
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.PhotoUrl
import com.example.myapp.data.model.ProfileImage
import com.example.myapp.data.model.Tag
import com.example.myapp.data.model.User
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.utils.ex.toFormattedString
import com.wajahatiqbal.blurhash.BlurHashPainter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhotoDetailContent(
    photo: PhotoResponse,
    navController: NavController,
    placeholder: Painter? = null,
    onUserClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {

                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(photo.urls?.regular)
                    .crossfade(700)
                    .build()


                AsyncImage(
                    model = imageRequest,
                    contentDescription = photo.description,
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
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        //User info
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            photo.user?.username?.let { username ->
                                onUserClick(username)
                            }
                        }
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    AsyncImage(
                        model = photo.user?.profile_image?.medium,
                        contentDescription = "User Avatar",
                        placeholder = placeholder,
                        error = placeholder,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = photo.user?.name ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.download_2),
                        contentDescription = "Download"
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    //
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.book_mark_2),
                        contentDescription = "Bookmark"
                    )
                }
            }
        }

        //Stick
        item {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        //2 column

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    InfoItem("Camera", photo.exif?.model ?: "N/A")
                    InfoItem("Focal Length", photo.exif?.focal_length ?: "N/A")
                    InfoItem("ISO", photo.exif?.ios.toString() ?: "N/A")
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoItem("Aperture", "f/${photo.exif?.aperture}" ?: "N/A")
                    InfoItem("Shutter Speed", "${photo.exif?.exposure_time}s" ?: "N/A")
                    InfoItem("Dimensions", "${photo.width} x ${photo.height}")
                }
            }
        }

        //Stick
        item {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.Center
                ) {
                    InfoItem("Views", photo.downloads.toFormattedString())
                }

                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.Center
                ) {

                    InfoItem("Downloads", photo.downloads.toFormattedString())
                }

                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.Center
                ) {
                    InfoItem("Likes", photo.likes.toFormattedString())
                }

            }
        }

        //Stick
        item {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = photo.tags.orEmpty(),
                    key = { it.title ?: "" }
                ) { tag ->
                    TagChip(text = tag.title ?: "")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


@Preview
@Composable
fun PhotoDetailContent() {

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

    val fakeTag = listOf<Tag>(
        Tag("Tuan Anh"),
        Tag("Hi Anh"),
        Tag("Ba Anh"),
        Tag("Bon Anh"),
        Tag("Tuan Anh"),
        Tag("Hi Anh"),
        Tag("Ba Anh"),
        Tag("Bon Anh"),
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
        tags = fakeTag,
        width = 3000,
        height = 6000,
        blur_hash = "123456",
        color = "123456"
    )

    MyAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

            val imageHolder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)

            PhotoDetailContent(
                photo = fakePhoto,
                navController = NavController(LocalContext.current),
                placeholder = imageHolder,
                onUserClick = {}
            )
        }
    }


}