package com.example.myapp.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.ui.components.PhotoListItem
import com.example.myapp.utils.UiState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun SearchPhotosPage(
    navController: NavController,
    sharedViewModel: SearchViewModel,
    photosViewModel: SearchPhotosViewModel = hiltViewModel()
) {
    val searchQuery by sharedViewModel.searchQuery.observeAsState()
    val searchResultState by photosViewModel.searchResult.collectAsStateWithLifecycle()
    val isLoadingMore by photosViewModel.isLoadingMore.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    LaunchedEffect(searchQuery) {
        searchQuery?.let {
            if (it.isNotEmpty()) {
                photosViewModel.searchPhotos(it)
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex != null) {
                    val totalItemsCount = listState.layoutInfo.totalItemsCount
                    if (lastVisibleItemIndex >= totalItemsCount - 10 && totalItemsCount > 0) {
                        photosViewModel.loadMorePhotos()
                    }
                }
            }
    }

    when (val state = searchResultState) {
        is UiState.Success -> {
            LazyColumn(
                state = listState
            ) {
                items(items = state.data.results, key = { it.id }) { photo ->
                    PhotoListItem(
                        photo = photo,
                        onItemClick = {
                            navController.navigate(Screen.Detail.createRoute(photo.id))
                        },
                        onUserClick = { userId ->
                            navController.navigate(Screen.Profile.createRoute(userId))
                        }
                    )
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieLoadingIndicator()
                        }
                    }
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