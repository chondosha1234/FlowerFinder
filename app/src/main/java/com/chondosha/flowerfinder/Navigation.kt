package com.chondosha.flowerfinder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chondosha.flowerfinder.ui.PhotoDetailScreen
import com.chondosha.flowerfinder.ui.PhotoListScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "photo_list"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("photo_list") {
            PhotoListScreen(
                modifier = Modifier,
                onNavigateToDetail = {
                    navController.navigate("photo_detail")
                }
            )
        }
        composable("photo_detail") {
            PhotoDetailScreen(
                modifier = Modifier
            )
        }
    }
}