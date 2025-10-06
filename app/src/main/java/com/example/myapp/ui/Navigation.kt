package com.example.myapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.MainViewModel
import com.example.myapp.ui.detail.DetailScreen
import com.example.myapp.ui.home.HomeScreen
import com.example.myapp.ui.home.HomeViewModel


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Collection : Screen("collection")
    object Profile : Screen("profile")
    object Detail: Screen("detail")
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

        composable(Screen.Detail.route) {
            DetailScreen(
                navController = navController
            )
        }

    }

}