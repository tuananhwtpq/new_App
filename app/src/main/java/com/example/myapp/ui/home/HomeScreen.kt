package com.example.myapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.Surface
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import com.example.myapp.R
import java.nio.file.WatchEvent

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

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    //region ALERT DIALOG
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

    //region MODAL BOTTOM SHEET
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            BottomSheetContent()
        }

    ) {

        Scaffold(
            topBar = {
                //region TOPBAR
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

            //region FAB
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,

            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = { },
                    backgroundColor = Color.Black
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                }
            },

            //region BOTTOM BAR

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
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                            }) {
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
            //region CONTENT
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
                            CollectionScreen(
                                navController = navController,
                                viewModel = homeViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

//region HOMETABCONTENT
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

//region PHOTOSCREEN

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


//region COLLECTION SCREEN
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


//region COLLECTION TAB CONTENT
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

//region BOTTOM SHEET CONTENT
@Composable
fun BottomSheetContent() {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.logo),
                contentDescription = "App Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text("Resplash_Clone", style = MaterialTheme.typography.bodyMedium)
                Text("Powered by creators everywhere", style = MaterialTheme.typography.bodyMedium)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        BottomSheetMenuItem(icon = Icons.Default.Settings, text = "Auto Wallpaper")
        BottomSheetMenuItem(icon = Icons.Default.Star, text = "Upgrade to Resplash Pro")
        BottomSheetMenuItem(icon = Icons.Default.Settings, text = "Settings")
        BottomSheetMenuItem(icon = Icons.Default.Info, text = "About")
    }

}

//region BOTTOM SHEET MENU
@Composable
fun BottomSheetMenuItem(icon: ImageVector, text: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(imageVector = icon, contentDescription = text, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text)

    }

}

//region PREVIEW HOME SCREEN
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

//region PREVIEW BOTTOM SHEET CONTENT
@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    MyAppTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomSheetContent()

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeTabContentPreview(){
//
//    val fakeListPhoto = listOf<PhotoResponse>(
//        PhotoResponse()
//    )
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ){
//        HomeTabContent(
//            photos = fakeListPhoto,
//            onPhotoClick = {  },
//            onUserClick = {  }
//        )
//    }
//}