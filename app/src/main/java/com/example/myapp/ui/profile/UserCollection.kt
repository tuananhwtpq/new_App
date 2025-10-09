package com.example.myapp.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.ui.components.CollectionListItem
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.utils.UiState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun UserCollection(
    state: UiState<List<CollectionResponse>>,
    isLoadingUserCollection: Boolean,
    onLoadMoreUserCollection: () -> Unit,
    onCollectionClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {

    when (state) {
        is UiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { LottieLoadingIndicator() }

        is UiState.Error -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text(state.message) }

        is UiState.Success -> {

            val listState = rememberLazyListState()

            LaunchedEffect(listState) {
                snapshotFlow {
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                }
                    .distinctUntilChanged()
                    .collect { lastVisibleItemIndex ->
                        if (lastVisibleItemIndex != null) {
                            val totalItemsCount = listState.layoutInfo.totalItemsCount
                            if (lastVisibleItemIndex >= totalItemsCount - 10 && totalItemsCount > 0) {
                                onLoadMoreUserCollection()
                            }
                        }
                    }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {

                items(items = state.data, key = { it.id }) { collection ->

                    CollectionListItem(
                        collection = collection,
                        onItemClick = onCollectionClick,
                        onUserClick = onUserClick
                    )
                }

                if (isLoadingUserCollection) {
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

        else -> {}
    }

}
