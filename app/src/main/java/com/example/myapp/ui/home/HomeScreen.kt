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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.BottomSheetContent
import com.example.myapp.ui.components.CollectionScreen
import com.example.myapp.ui.components.FabCutoutShape
import com.example.myapp.ui.components.PhotoScreen
import com.example.myapp.ui.components.SortDialog
import com.example.myapp.ui.theme.MyAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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

    val collectionSortOption = listOf("ALL")
    var selectedCollectionSortOption by remember { mutableStateOf(collectionSortOption[0]) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }

    val photoListState = rememberLazyListState()
    val collectionListState = rememberLazyListState()

    //region DIALOG
    if (showSortDialog) {
        when (pageState.currentPage) {
            0 -> {
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

            1 -> {
                SortDialog(
                    selectedOption = selectedCollectionSortOption,
                    sortOptions = collectionSortOption,
                    onOptionSelected = { option ->
                        selectedCollectionSortOption = option
                        showSortDialog = false
                    },
                    onDismiss = {
                        showSortDialog = false
                    }
                )
            }
        }

    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            BottomSheetContent()
        }
    }

    //region MODAL BOTTOM SHEET
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            //region TOPBAR
            TabRow(
                selectedTabIndex = pageState.currentPage,
                modifier = Modifier.statusBarsPadding(),
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

        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(
                    //animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
                    initialOffsetY = { it }),
                exit = slideOutVertically(
                    //animationSpec = tween(durationMillis = 100, easing = FastOutLinearInEasing),
                    targetOffsetY = { it }),
            ) {
                Box() {
                    //region BOTTOM APP BAR
                    BottomAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(FabCutoutShape()),
                        tonalElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row {
                                IconButton(onClick = {
                                    showBottomSheet = true
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

                    //region FAB
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = { },
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-28).dp)
                            .animateEnterExit(
                                enter = scaleIn() + fadeIn(),
                                exit = scaleOut() + fadeOut()
                            )
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            }

        }

    ) { paddingValues ->
        //region CONTENT
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
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
                            listState = photoListState,
                            paddingValues = paddingValues
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
                            listState = collectionListState,
                            paddingValues = paddingValues
                        )
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
//            HomeScreen(
//                navController = NavController(LocalContext.current),
//                homeViewModel = hiltViewModel<HomeViewModel>()
//            )
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
