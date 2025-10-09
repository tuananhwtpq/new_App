package com.example.myapp.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.room.util.query
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.utils.UiState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.CollectionListItem
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun SearchCollectionPage(
    navController: NavController,
    sharedViewModel: SearchViewModel,
    collectionViewModel: SearchCollectionViewModel = hiltViewModel()
) {

    val searchQuery by sharedViewModel.searchQuery.observeAsState()
    val seachResultState by collectionViewModel.result.collectAsStateWithLifecycle()
    val isLoadingMoreState by collectionViewModel.isLoadingMore.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    LaunchedEffect(searchQuery) {
        searchQuery?.let {
            if (it.isNotEmpty()) {
                collectionViewModel.seachCollections(it)
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
                        collectionViewModel.loadMoreCollections()
                    }
                }
            }
    }

    when (val state = seachResultState) {
        is UiState.Error -> {

        }

        is UiState.Init -> {

        }

        is UiState.Loading -> {
            LottieLoadingIndicator()
        }

        is UiState.Success -> {
            LazyColumn(
                state = listState
            ) {
                items(items = state.data.results, key = { it.id }) { collection ->
                    CollectionListItem(
                        collection = collection,
                        onItemClick = {
                            navController.navigate(Screen.CollectionDetail.createRoute(collection.id))
                        },
                        onUserClick = {
                            navController.navigate(Screen.Profile.createRoute(collection.user.username.toString()))
                        }
                    )

                }

                if (isLoadingMoreState) {
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