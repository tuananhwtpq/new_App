package com.example.myapp.ui.search

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.utils.UiState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.UserSearchResultItem
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun SearchUserPage(
    navController: NavController,
    sharedViewModel: SearchViewModel,
    seachViewModel: SearchUserViewModel = hiltViewModel()
) {

    val searchQuery by sharedViewModel.searchQuery.observeAsState()
    val searchResultState by seachViewModel.searchUserResult.collectAsStateWithLifecycle()
    val isLoadingMore by seachViewModel.isLoadingMore.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    LaunchedEffect(searchQuery) {
        searchQuery?.let {
            if (it.isNotEmpty()) {
                seachViewModel.searchUser(it.toString())
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
                        seachViewModel.loadMoreUsers()
                    }
                }
            }
    }

    when (val state = searchResultState) {
        is UiState.Error -> {
            Log.e("SearchUserPage", "Error: ${state.message}")
        }

        is UiState.Init -> {
            EmptyState()
        }

        is UiState.Loading -> {
            LottieLoadingIndicator()
        }

        is UiState.Success -> {

            if (state.data.isEmpty()) {
                EmptyState()
            }

            LazyColumn(
                state = listState
            ) {
                items(items = state.data, key = { it.user.id }) { userWithPhoto ->
                    UserSearchResultItem(
                        userWithPhoto = userWithPhoto,
                        onUserClick = {
                            navController.navigate(Screen.Profile.createRoute(userWithPhoto.user.username.toString()))
                        },
                        placeholder = null,
                        onPhotoClick = { photoId ->
                            navController.navigate(Screen.Detail.createRoute(photoId))
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
    }

}