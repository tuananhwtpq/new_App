package com.example.myapp.ui.components

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.ui.Screen
import com.example.myapp.ui.home.HomeViewModel
import com.example.myapp.utils.Constants.SCROLL_THRESHOLD
import com.example.myapp.utils.UiState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    onScroll: (Boolean) -> Unit,
    listState: LazyListState,
    paddingValues: PaddingValues
) {
    val uiState by viewModel.photoList.collectAsState()
    val isRefreshing by viewModel.isRefreshingPhoto.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    var previousScrollOffset by rememberSaveable { mutableStateOf(0) }

//region SWIPE LIST
    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousOffset = listState.firstVisibleItemScrollOffset

        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .collect { (currentIndex, currentOffset) ->
                if (currentIndex == 0 && currentOffset == 0) {
                    onScroll(true)
                } else if (currentIndex > previousIndex) {
                    onScroll(false)
                } else if (currentIndex < previousIndex) {
                    onScroll(true)
                }
                // Cuộn trong cùng 1 item
                else {
                    val scrollDelta = currentOffset - previousOffset
                    if (abs(scrollDelta) > SCROLL_THRESHOLD) {
                        if (scrollDelta > 0) {
                            onScroll(false)
                        } else {
                            onScroll(true)
                        }
                    }
                }

                previousIndex = currentIndex
                previousOffset = currentOffset
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
                        viewModel.loadMorePhotos()
                    }
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshPhoto() }
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                LottieLoadingIndicator()
            }

            is UiState.Success -> {
                HomeTabContent(
                    photos = state.data, onPhotoClick = { photoId ->
                        navController.navigate(Screen.Detail.createRoute(photoId))
                    },
                    onUserClick = { username ->
                        navController.navigate(Screen.Profile.createRoute(username))
                    },
                    listState = listState,
                    isLoadingMore = isLoadingMore,
                    paddingValues = paddingValues
                )
            }

            is UiState.Error -> {
                Log.d("PhotoScreen", "Lỗi: $state")
            }

            UiState.Init -> {

            }
        }
    }

}

//region HOMETABCONTENT
@Composable
fun HomeTabContent(
    photos: List<PhotoResponse>,
    onPhotoClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    isLoadingMore: Boolean,
    paddingValues: PaddingValues
) {


    val bottomPadding = paddingValues.calculateBottomPadding()
    val animatedBottomPadding by animateDpAsState(
        targetValue = bottomPadding,
        label = "bottom_padding_animation"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = animatedBottomPadding + 8.dp
        ),
        state = listState
    ) {
        items(
            items = photos,
            key = { photo -> photo.id }
        ) { photo ->
            PhotoListItem(
                photo = photo, onItemClick = onPhotoClick,
                placeholder = null,
                onUserClick = onUserClick
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