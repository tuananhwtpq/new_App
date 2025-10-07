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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.CoverPhoto
import com.example.myapp.data.model.PhotoUrl
import com.example.myapp.data.model.ProfileImage
import com.example.myapp.data.model.User
import com.example.myapp.ui.theme.MyAppTheme
import java.nio.file.WatchEvent

@Composable
fun CollectionListItem(
    collection: CollectionResponse,
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
                .clickable(
                    onClick = {
                        collection.user.username.let {
                            onUserClick(it.toString())
                        }
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = collection.user.profile_image?.medium,
                contentDescription = "User avatar",
                placeholder = placeholder,
                error = placeholder,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = collection.user.name ?: "Unknown User",
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable() {
                    onItemClick(collection.id)
                },
            shape = RoundedCornerShape(16.dp),

            ) {

            Box(modifier = Modifier.fillMaxSize()) {

                AsyncImage(
                    model = collection.cover_photo?.urls?.regular,
                    contentDescription = collection.description ?: "Unknown collection",
                    placeholder = placeholder,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop

                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = collection.title ?: "Untitled Collection",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${collection.total_photos ?: 0} Photos",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }

    }
}

@Preview
@Composable
fun CollectionListItem() {

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

    val newCoverPhoto = CoverPhoto(
        id = "12",
        urls = PhotoUrl(
            raw = "",
            full = "",
            regular = "https://res.cloudinary.com/dguxc4au3/image/upload/v1754787853/78b303974d8b705c370d57a9f9bd48ca_gwxnbs.jpg",
            small = "",
            thumb = null,
            small_s3 = null
        ),
    )

    val newCollection = CollectionResponse(
        id = "2",
        title = "outside.",
        description = "none of",
        total_photos = 1000,
        cover_photo = newCoverPhoto,
        user = newUser,
        links = null
    )

    MyAppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {

            val imageHolder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)

            CollectionListItem(
                collection = newCollection,
                onItemClick = {},
                placeholder = imageHolder,
                onUserClick = {}
            )
        }
    }

}