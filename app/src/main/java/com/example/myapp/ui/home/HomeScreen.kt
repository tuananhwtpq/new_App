package com.example.myapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.data.model.PhotoResponse
import com.example.myapp.ui.components.PhotoListItem
import com.example.myapp.utils.UiState
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ModalBottomSheet
import com.example.myapp.data.model.CollectionResponse
import com.example.myapp.ui.components.CollectionListItem


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
        bottomBar = {
            //My bottom navBar
        },
        floatingActionButton = {
            //Floating action button
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
                        when (val state = uiState) {
                            is UiState.Error -> {
                                Text(
                                    text = state.message ?: "Unknown error",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            is UiState.Init -> {

                            }

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
                                HomeTabContent(photos = state.data, onPhotoClick = {
                                    // navController.navigate("photoDetail/${it}")
                                })
                            }
                        }
                    }
                    // Collection Screen
                    1 -> {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Text(text = "Collection")
                        }
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
            PhotoListItem(photo = photo, onItemClick = onPhotoClick)

        }
    }
}

@Composable
fun PhotoScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.photoList.collectAsState()

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
            HomeTabContent(photos = state.data, onPhotoClick = {
                // navController.navigate("photoDetail/${it}")
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

//@Composable
//fun CollectionScreen(viewModel: CollectionViewModel = hiltViewModel()) {
//    val uiState by viewModel.collectionList.collectAsState()
//
//    when (val state = uiState) {
//        is UiState.Loading -> {
//
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                )
//            }
//        }
//
//        is UiState.Success -> {
//            CollectionTabContent(collection = state.data, onCollectionClick = {
//                // navController.navigate("photoDetail/${it}")
//            })
//        }
//
//        is UiState.Error -> {
//            Text(
//                text = state.message ?: "Unknown error",
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//
//        UiState.Init -> {
//
//        }
//    }
//
//
//}
//
//
//@Composable
//fun CollectionTabContent(
//    collection: List<CollectionResponse>,
//    onCollectionClick: (String) -> Unit
//) {
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(vertical = 8.dp)
//    ) {
//        items(
//            items = collection,
//            key = { collection -> collection.id }
//        ) { collection ->
//            CollectionListItem(collection = collection, onItemClick = onCollectionClick)
//        }
//    }
//
//}