package com.example.myapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.CollectionListItem
import com.example.myapp.ui.components.PhotoListItem
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.utils.UiState
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
) {
    val tabTitles = listOf("PHOTOS", "COLLECTIONS")
    val uiState by homeViewModel.photoList.collectAsState()
    val pageState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = pageState.currentPage) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pageState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pageState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) }
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }

                    Row {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                        }
                    }
                }
            }
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(state = pageState) { page ->
                when (page) {
                    //Photo Screen
                    0 -> {
                        PhotoScreen(navController = navController, viewModel = homeViewModel)
                    }
                    // Collection Screen
                    1 -> {
                        CollectionScreen(navController = navController, viewModel = homeViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTabContent(photos: List<PhotoResponse>, onPhotoClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = photos,
            key = { photo -> photo.id }
        ) { photo ->
            PhotoListItem(
                photo = photo, onItemClick = onPhotoClick,
                placeholder = null,
                onUserClick = {}
//                onUserClick = {username ->
//                    navController.navigate(Screen.Profile.createRoute(username))
//                }
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.photoList.collectAsState()
    val isRefreshing by viewModel.isRefreshingPhoto.collectAsState()


    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshPhoto() }
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> {
                HomeTabContent(photos = state.data, onPhotoClick = { photoId ->
                    navController.navigate(Screen.Detail.createRoute(photoId))
                })
            }

            is UiState.Error -> {
                Text(
                    text = state.message ?: "Unknown error",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            UiState.Init -> {

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.collectionList.collectAsState()

    val isRefreshing by viewModel.isRefreshingCollection.collectAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshCollection() }
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> {
                CollectionTabContent(collection = state.data, onCollectionClick = { collectionId ->
                    navController.navigate(Screen.CollectionDetail.createRoute(collectionId))
                })
            }

            is UiState.Error -> {
                Text(
                    text = state.message ?: "Unknown error",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            UiState.Init -> {}
        }
    }
}


@Composable
fun CollectionTabContent(
    collection: List<CollectionResponse>,
    onCollectionClick: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = collection,
            key = { collection -> collection.id }
        ) { collection ->
            CollectionListItem(collection = collection, onItemClick = onCollectionClick)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            HomeScreen(
                navController = NavController(LocalContext.current),
                homeViewModel = hiltViewModel<HomeViewModel>()
            )
        }
    }

}