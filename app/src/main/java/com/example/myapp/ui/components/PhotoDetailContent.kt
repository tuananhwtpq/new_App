package com.example.myapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapp.data.model.PhotoResponse

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhotoDetailContent(
    photo: PhotoResponse,
    navController: NavController
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
                AsyncImage(
                    model = photo.urls?.regular,
                    contentDescription = photo.description,
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
                AsyncImage(
                    model = photo.user?.profile_image?.medium,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(12.dp))
                Text(photo.user?.name ?: "Unknown", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Done, contentDescription = "Download")
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    //
                    Icon(Icons.Default.AccountBox, contentDescription = "Bookmark")
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoItem("Views", photo.downloads?.toString() ?: "0")
                InfoItem("Downloads", photo.downloads?.toString() ?: "0")
                InfoItem("Likes", photo.likes?.toString() ?: "0")
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

//        item {
//            LazyRow(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//            ) {
//                items(
//                    items = photo.tags.orEmpty(),
//                    key = { it.title ?: "" }
//                ) { tag ->
//                    TagChip(text = tag.title ?: "")
//                }
//            }
//        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

