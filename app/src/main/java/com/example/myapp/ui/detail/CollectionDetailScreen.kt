package com.example.myapp.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapp.ui.Screen
import com.example.myapp.ui.components.LottieLoadingIndicator
import com.example.myapp.ui.components.PhotoListItem
import com.example.myapp.ui.home.CollectionDetailViewModel
import com.example.myapp.ui.search.EmptyState
import com.example.myapp.utils.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    navController: NavController,
    viewModel: CollectionDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.collectionList.collectAsState()
    val collectionInfo by viewModel.collectionInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    when (val state = collectionInfo) {
                        is UiState.Error -> {
                            Text(text = "Error Loading Title")
                        }

                        is UiState.Success -> {
                            Column {
                                Text(
                                    text = state.data.title.toString(),
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        else -> {}
                    }
                },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LottieLoadingIndicator()
                }
            }

            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues
                ) {

                    item {
                        when (val state = collectionInfo) {
                            is UiState.Success -> {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${state.data.total_photos} Photos • Curated by ${state.data.user.name}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            }

                            else -> {}
                        }
                    }

                    items(items = state.data, key = { it.id }) { photo ->
                        PhotoListItem(
                            photo = photo, onItemClick = { photoId ->
                                navController.navigate(Screen.Detail.createRoute(photoId))
                            },
                            placeholder = null,
                            onUserClick = { username ->
                                navController.navigate(Screen.Profile.createRoute(username))
                            }
                        )
                    }
                }
            }

            is UiState.Error -> {
                EmptyState()
            }

            else -> {}
        }
    }
}