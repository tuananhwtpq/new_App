package com.example.myapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.CollectionListItem
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.ui.components.PhotoListItem
import com.example.myapp.ui.theme.MyAppTheme
import com.example.myapp.utils.UiState
import kotlinx.coroutines.launch
import androidx.compose.material.ModalBottomSheetLayout

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

    var showSortDialog by remember { mutableStateOf(false) }
    val sortOptions = listOf("Latest", "Oldest", "Popular")
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }

    //val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Sort by") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sortOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSortOption = option
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = (option == selectedSortOption),
                                onClick = {
                                    selectedSortOption = option
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSortDialog = false
                        //homeViewModel.refreshPhoto()
                        homeViewModel.sortPhotos(selectedSortOption.lowercase())
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }


    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = pageState.currentPage,
                modifier = Modifier.statusBarsPadding(),
                backgroundColor = Color.White
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pageState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pageState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title, color = Color.Black) }
                    )
                }
            }
        },


        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,

        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {  },
                backgroundColor = Color.Black
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
            }
        },

        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape,
                backgroundColor = Color.White,
                elevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }

                    Row {
                        IconButton(onClick = {
                            navController.navigate(Screen.Search.route)
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = {
                            showSortDialog = true
                        }) {
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
fun HomeTabContent(
    photos: List<PhotoResponse>,
    onPhotoClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
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
                onUserClick = onUserClick
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
                LottieLoadingIndicator()
            }

            is UiState.Success -> {
                HomeTabContent(
                    photos = state.data, onPhotoClick = { photoId ->
                        navController.navigate(Screen.Detail.createRoute(photoId))
                    },
                    onUserClick = { username ->
                        navController.navigate(Screen.Profile.createRoute(username))
                    }
                )
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

                LottieLoadingIndicator()
            }

            is UiState.Success -> {
                CollectionTabContent(collection = state.data, onCollectionClick = { collectionId ->
                    navController.navigate(Screen.CollectionDetail.createRoute(collectionId))
                }, onUserClick = {
                    navController.navigate(Screen.Profile.createRoute(it))
                })
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


@Composable
fun CollectionTabContent(
    collection: List<CollectionResponse>,
    onCollectionClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
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