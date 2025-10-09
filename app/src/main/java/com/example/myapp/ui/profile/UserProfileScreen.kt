package com.example.myapp.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.ProfileImage
import com.example.myapp.data.model.User
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.utils.UiState

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
    val userCollectionState by viewModel.userCollection.collectAsState()
    val isLoadingUserCollection by viewModel.isLoadingUserCollection.collectAsState()

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
                        onLoadMoreLikePhotos = { viewModel.loadMoreLikePhoto() },
                        isLoadingUserCollection = isLoadingUserCollection,
                        onLoadMoreUserCollection = { viewModel.loadMoreUserCollection() },
                        userCollectionState = userCollectionState,
                        onCollectionClick = {
                            navController.navigate(Screen.CollectionDetail.createRoute(it))
                        },
                        onUserClick = {
                            //navController.navigate(Screen.Profile.createRoute(state.data.username.toString()))
                        },
                        navController = navController
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
        blur_hash = "123456",
        color = "123456"
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
            onLoadMoreLikePhotos = { },
            isLoadingUserCollection = true,
            onLoadMoreUserCollection = {},
            userCollectionState = UiState.Success(emptyList()),
            onCollectionClick = {},
            onUserClick = {},
            navController = NavController(LocalContext.current)
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
            onLoadMoreLikePhotos = { },
            isLoadingUserCollection = true,
            onLoadMoreUserCollection = {},
            userCollectionState = UiState.Loading,
            onCollectionClick = {},
            onUserClick = {},
            navController = NavController(LocalContext.current)
        )
    }
}