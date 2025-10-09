package com.example.myapp.ui.profile

import android.R.attr.translationY
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.data.model.User
import com.example.myapp.ui.components.InfoItem2
import com.example.myapp.utils.UiState
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserProfileContent(
    user: User,
    photosState: UiState<List<PhotoResponse>>,
    userLikePhotoState: UiState<List<PhotoResponse>>,
    userCollectionState: UiState<List<CollectionResponse>>,
    isLoadingMore: Boolean,
    isLoadingLikePhoto: Boolean,
    isLoadingUserCollection: Boolean,
    onLoadMorePhotos: () -> Unit,
    onLoadMoreLikePhotos: () -> Unit,
    onLoadMoreUserCollection: () -> Unit
) {
    //Chiều cao phần user info
    var userInfoHeightPx by remember { mutableFloatStateOf(0f) }

    //vị trí offset của header
    val headerOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = headerOffsetHeightPx.floatValue + delta

                headerOffsetHeightPx.floatValue = newOffset.coerceIn(-userInfoHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    val tabs = remember(user) {
        mutableListOf("PHOTOS").apply {
            user.total_likes?.let { if (it > 0) add("LIKES") }
            user.total_collections?.let { if (it > 0) add("COLLECTIONS") }
        }
    }
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {

        //region TAB ROW & PAGER
        Column(
            modifier = Modifier.graphicsLayer {
                translationY = userInfoHeightPx + headerOffsetHeightPx.floatValue
            }
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = title, color = Color.Black) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                when (tabs[pageIndex]) {
                    "PHOTOS" -> {
                        UserPhotosGrid(
                            state = photosState,
                            isLoadingMore = isLoadingMore,
                            onLoadMore = onLoadMorePhotos
                        )
                    }

                    "LIKES" -> {
                        UserLikePhoto(
                            state = userLikePhotoState,
                            isLoadingLikePhoto = isLoadingLikePhoto,
                            onLoadMoreLikePhotos = onLoadMoreLikePhotos
                        )
                    }

                    "COLLECTIONS" -> {
                        UserCollection(
                            state = userCollectionState,
                            isLoadingUserCollection = isLoadingUserCollection,
                            onLoadMoreUserCollection = onLoadMoreUserCollection,
                            onCollectionClick = {},
                            onUserClick = { }
                        )
                    }
                }
            }
        }

        //region User Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = headerOffsetHeightPx.floatValue
                }
                .background(MaterialTheme.colorScheme.surface)
                .onSizeChanged { size ->
                    userInfoHeightPx = size.height.toFloat()
                }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = user.profile_image?.large,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    InfoItem2(title = "Photos", value = user.total_photos.toString())
                    InfoItem2(title = "Likes", value = user.total_likes.toString())
                    InfoItem2(title = "Collections", value = user.total_collections.toString())
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = user.name ?: "", fontWeight = FontWeight.Bold)
                if (!user.location.isNullOrEmpty()) {
                    Text(text = user.location, style = MaterialTheme.typography.bodySmall)
                }
                if (!user.bio.isNullOrEmpty()) {
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}