package com.example.myapp.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.ui.components.PhotoDetailContent
import com.example.myapp.utils.UiState

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel(),
    photoId: String?
) {
    val uiState by viewModel.imageInfo.collectAsState()


    LaunchedEffect(key1 = photoId) {
        if (!photoId.isNullOrEmpty()){
            viewModel.getPhotoById(photoId)
        }
    }

    Scaffold(
        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                onClick = { /* TODO: Handle set as wallpaper */ },
//                //icon = { Icon(Icons.Filled.Wallpaper, contentDescription = "Set as wallpaper") },
//                text = { Text(text = "SET AS WALLPAPER") }
//            )
        },
        floatingActionButtonPosition = FabPosition.Center
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
                    PhotoDetailContent(photo = state.data, navController = navController)
                }
                is UiState.Error -> {
                    Text(
                        text = state.message ?: "An error occurred",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }
    }
}


