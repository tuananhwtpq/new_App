package com.example.myapp.ui.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.User
import com.example.myapp.ui.components.InfoItem
import com.example.myapp.utils.UiState
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.data.model.ProfileImage
import com.example.myapp.ui.components.InfoItem2
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.ui.theme.MyAppTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.TODO

//region USER PROFILE SCREEN
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = hiltViewModel()
) {

    val userProfileState by viewModel.userProfile.collectAsState()
    val userPhotosState by viewModel.userListPhoto.collectAsState()
    val userLikePhoto by viewModel.userLikePhoto.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    val isLoadingLikePhoto by viewModel.isLoadingLikePhoto.collectAsState()

    Scaffold(
        topBar = {
            when (val state = userProfileState) {
                is UiState.Success -> {
                    TopAppBar(
                        title = { Text(state.data.username ?: "") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Info, contentDescription = "open browser")
                            }

                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Share, contentDescription = "share profile")
                            }
                        }
                    )
                }

                else -> {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = userProfileState) {
                is UiState.Loading -> LottieLoadingIndicator()
                is UiState.Error -> Text(state.message ?: "Error")
                is UiState.Success -> {
                    UserProfileContent(
                        user = state.data,
                        photosState = userPhotosState,
                        userLikePhotoState = userLikePhoto,
                        isLoadingMore = isLoadingMore,
                        onLoadMorePhotos = { viewModel.loadMorePhoto() },
                        isLoadingLikePhoto = isLoadingLikePhoto,
                        onLoadMoreLikePhotos = { viewModel.loadMoreLikePhoto() }
                    )
                }

                else -> {}
            }
        }
    }
}

//region PREVIEW
private val fakeUser = User(
    id = "1",
    username = "fakeuser",
    name = "Fake User Name",
    bio = "This is a fake bio for preview purposes. I love taking photos.",
    location = "Android Studio",
    total_likes = 1200,
    total_photos = 487,
    total_collections = 3,
    profile_image = ProfileImage(
        small = "",
        medium = "",
        large = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80"
    ),
    first_name = "hehe",
    last_name = null,
    twitter_username = null,
    portfolio_url = null,
    instagram_username = null,
    downloads = null,
    social = null
)

private val fakePhotos = listOf(
    PhotoResponse(
        id = "10",
        description = null,
        urls = null,
        user = fakeUser,
        downloads = 1000,
        likes = 1000,
        exif = null,
        tags = null,
        width = 3000,
        height = 6000,
        blur_hash = "123456"
    )
)

@Preview(showBackground = true)
@Composable
fun UserProfileContentPreview() {
    MyAppTheme {
        UserProfileContent(
            user = fakeUser,
            photosState = UiState.Success(fakePhotos),
            userLikePhotoState = UiState.Success(fakePhotos),
            isLoadingMore = true,
            onLoadMorePhotos = { },
            isLoadingLikePhoto = true,
            onLoadMoreLikePhotos = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileContentLoadingPreview() {
    MyAppTheme {
        UserProfileContent(
            user = fakeUser,
            photosState = UiState.Loading,
            userLikePhotoState = UiState.Loading,
            isLoadingMore = true,
            onLoadMorePhotos = { },
            isLoadingLikePhoto = true,
            onLoadMoreLikePhotos = { }
        )
    }
}