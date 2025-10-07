package com.example.myapp.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.PhotoDetailContent
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.utils.UiState

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel(),
    photoId: String?
) {
    val uiState by viewModel.imageInfo.collectAsState()


    LaunchedEffect(key1 = photoId) {
        if (!photoId.isNullOrEmpty()) {
            viewModel.getPhotoById(photoId)
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { },
                containerColor = Color.Black,
                contentColor = Color.White,
                icon = {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "Set as wallpaper icon"
                    )
                },
                text = { Text(text = "SET AS WALLPAPER") }
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is UiState.Success -> {
                    PhotoDetailContent(
                        photo = state.data, navController = navController,
                        onUserClick = { username ->
                            navController.navigate(Screen.Profile.createRoute(username))
                        })
                }

                is UiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {}
            }
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {

    MyAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            DetailScreen(
                navController = NavController(LocalContext.current),
                viewModel = hiltViewModel<DetailViewModel>(),
                photoId = "99283",
            )
        }
    }
}


