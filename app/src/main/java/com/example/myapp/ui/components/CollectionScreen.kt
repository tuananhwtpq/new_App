package com.example.myapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.ui.Screen
import com.example.myapp.ui.home.HomeViewModel
import com.example.myapp.utils.Constants.SCROLL_THRESHOLD
import com.example.myapp.utils.UiState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    navController: NavController, viewModel: HomeViewModel = hiltViewModel(),
    onScroll: (Boolean) -> Unit,
    listState: LazyListState
) {
    val uiState by viewModel.collectionList.collectAsState()

    val isRefreshing by viewModel.isRefreshingCollection.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    //val listState = rememberLazyListState()
    var previousScrollOffset by rememberSaveable { mutableStateOf(0) }

    //region SWIPE LIST
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .map { it to (listState.firstVisibleItemIndex == 0) }
            .distinctUntilChanged()
            .collect { (currentOffset, isAtTop) ->
                if (isAtTop) {
                    onScroll(true)
                } else {
                    val scrollDelta = currentOffset - previousScrollOffset

                    if (scrollDelta > SCROLL_THRESHOLD) {
                        onScroll(false)
                    } else if (abs(scrollDelta) > SCROLL_THRESHOLD) {
                        onScroll(true)
                    }
                    previousScrollOffset = currentOffset
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
                    if (lastVisibleItemIndex >= totalItemsCount - 5 && totalItemsCount > 0) {
                        viewModel.loadMoreCollections()
                    }
                }
            }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshCollection() }
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {

                LottieLoadingIndicator()
            }

            is UiState.Success -> {
                CollectionTabContent(
                    collection = state.data, onCollectionClick = { collectionId ->
                        navController.navigate(Screen.CollectionDetail.createRoute(collectionId))
                    }, onUserClick = {
                        navController.navigate(Screen.Profile.createRoute(it))
                    },
                    listState = listState,
                    isLoadingMore = isLoadingMore
                )
            }

            is UiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            UiState.Init -> {}
        }
    }
}


//region COLLECTION TAB CONTENT
@Composable
fun CollectionTabContent(
    collection: List<CollectionResponse>,
    onCollectionClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    isLoadingMore: Boolean
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        state = listState
    ) {
        items(
            items = collection,
            key = { collection -> collection.id }
        ) { collection ->
            CollectionListItem(
                collection = collection, onItemClick = onCollectionClick,
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
                    CircularProgressIndicator()
                }
            }
        }
    }

}