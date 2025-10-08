package com.example.myapp.ui.search

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
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.CollectionListItem


@Composable
fun SearchCollectionPage(
    navController: NavController,
    sharedViewModel: SearchViewModel,
    collectionViewModel: SearchCollectionViewModel = hiltViewModel()
) {

    val searchQuery by sharedViewModel.searchQuery.observeAsState()
    val seachResultState by collectionViewModel.result.collectAsState()

    LaunchedEffect(searchQuery) {
        searchQuery?.let {
            if (it.isNotEmpty()) {
                collectionViewModel.searchCollections(it)
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
            LazyColumn {
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
            }
        }
    }


}