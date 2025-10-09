package com.example.myapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.BottomSheetContent
import com.example.myapp.ui.components.CollectionScreen
import com.example.myapp.ui.components.PhotoScreen
import com.example.myapp.ui.components.SortDialog
import com.example.myapp.ui.theme.MyAppTheme
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
    var showSortDialog by remember { mutableStateOf(false) }
    val sortOptions = listOf("Latest", "Oldest", "Popular")
    var selectedSortOption by remember { mutableStateOf(sortOptions[0]) }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }

    val photoListState = rememberLazyListState()
    val collectionListState = rememberLazyListState()

    //region DIALOG
    if (showSortDialog) {
        SortDialog(
            selectedOption = selectedSortOption,
            sortOptions = sortOptions,
            onOptionSelected = { option ->
                selectedSortOption = option
                homeViewModel.sortPhotos(option.lowercase())
                showSortDialog = false
            },
            onDismiss = {
                showSortDialog = false
            }
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
            modifier = Modifier.navigationBarsPadding(),
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

                                    if (pageState.currentPage == index) {
                                        when (index) {
                                            0 -> {
                                                photoListState.scrollToItem(0)
                                            }

                                            1 -> {
                                                collectionListState.scrollToItem(0)
                                            }
                                        }
                                    } else {
                                        pageState.animateScrollToPage(index)
                                    }
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

                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = { },
                        backgroundColor = Color.Black
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                    }
                }
            },

            //region BOTTOM BAR

            bottomBar = {
                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                    content = {
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
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = "More Options"
                                        )
                                    }
                                }
                            }
                        }
                    }
                )

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
                            PhotoScreen(
                                navController = navController, viewModel = homeViewModel,
                                onScroll = { visible ->
                                    isBottomBarVisible = visible
                                },
                                listState = photoListState
                            )
                        }
                        // Collection Screen
                        1 -> {
                            CollectionScreen(
                                navController = navController,
                                viewModel = homeViewModel,
                                onScroll = { visible ->
                                    isBottomBarVisible = visible
                                },
                                listState = collectionListState
                            )
                        }
                    }
                }
            }
        }
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
