package com.example.myapp.ui.search

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.ui.components.PhotoListItem
import com.example.myapp.utils.UiState

@Composable
fun SearchPhotosPage(
    navController: NavController,
    sharedViewModel: SearchViewModel,
    photosViewModel: SearchPhotosViewModel = hiltViewModel()
) {
    val searchQuery by sharedViewModel.searchQuery.observeAsState()
    val searchResultState by photosViewModel.searchResult.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery) {
        searchQuery?.let {
            if (it.isNotEmpty()) {
                photosViewModel.searchPhotos(it)
            }
        }
    }

    when (val state = searchResultState) {
        is UiState.Success -> {
            LazyColumn {
                items(items = state.data.results, key = { it.id }) { photo ->
                    PhotoListItem(
                        photo = photo,
                        onItemClick = {
                            navController.navigate(Screen.Detail.createRoute(photo.id))
                        },
                        onUserClick = {

                        }
                    )
                }
            }
        }

        is UiState.Loading -> {
            LottieLoadingIndicator()
        }

        is UiState.Error -> {}
        is UiState.Init -> {
            EmptyState()
        }
    }
}