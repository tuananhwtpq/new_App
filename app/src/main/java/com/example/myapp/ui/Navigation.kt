package com.example.myapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapp.MainViewModel
import com.example.myapp.ui.detail.CollectionDetailScreen
import com.example.myapp.ui.detail.DetailScreen
import com.example.myapp.ui.home.HomeScreen
import com.example.myapp.ui.home.HomeViewModel
import com.example.myapp.ui.profile.UserProfileScreen
import com.example.myapp.ui.search.SearchScreen


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Collection : Screen("collection")
    object Profile : Screen("profile/{username}") {
        fun createRoute(username: String) = "profile/$username"
    }

    object Detail : Screen("detail/{photoId}") {
        fun createRoute(photoId: String) = "detail/$photoId"
    }

    object CollectionDetail : Screen("collectionDetail/{collectionId}") {
        fun createRoute(collectionId: String) = "collectionDetail/$collectionId"
    }
}

@Composable
fun Navigation(modifier: Modifier) {

    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                homeViewModel = hiltViewModel<HomeViewModel>()
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("photoId") { type = NavType.StringType })
        ) { backStackEntry ->

            val photoId = backStackEntry.arguments?.getString("photoId")
            DetailScreen(
                navController = navController,
                photoId = photoId
            )
        }

        composable(
            route = Screen.CollectionDetail.route,
            arguments = listOf(navArgument("collectionId") { type = NavType.StringType })
        ) {

            CollectionDetailScreen(
                navController = navController,
            )

        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) {
            UserProfileScreen(
                navController = navController
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                navController = navController
            )
        }

    }

}
