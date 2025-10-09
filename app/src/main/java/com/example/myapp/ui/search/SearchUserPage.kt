package com.example.myapp.ui.search

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.utils.UiState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.UserSearchResultItem


@Composable
fun SearchUserPage(
    navController: NavController,
    sharedViewModel: SearchViewModel,
    seachViewModel: SearchUserViewModel = hiltViewModel()
) {

    val searchQuery by sharedViewModel.searchQuery.observeAsState()
    val searchResultState by seachViewModel.searchUserResult.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery) {
        searchQuery?.let {
            if (it.isNotEmpty()) {
                seachViewModel.searchUser(it.toString())
            }
        }
    }

    when (val state = searchResultState) {
        is UiState.Error -> {
            Log.e("SearchUserPage", "Error: ${state.message}")
        }

        is UiState.Init -> {
            EmptyState()
        }

        is UiState.Loading -> {
            LottieLoadingIndicator()
        }

        is UiState.Success -> {

            if (state.data.isEmpty()) {
                EmptyState()
            }

            LazyColumn {
                items(items = state.data, key = { it.user.id }) { userWithPhoto ->
                    UserSearchResultItem(
                        userWithPhoto = userWithPhoto,
                        onUserClick = {
                            navController.navigate(Screen.Profile.createRoute(userWithPhoto.user.username.toString()))
                        },
                        placeholder = null,
                        onPhotoClick = { photoId ->
                            navController.navigate(Screen.Detail.createRoute(photoId))
                        }
                    )
                }
            }
        }
    }

}