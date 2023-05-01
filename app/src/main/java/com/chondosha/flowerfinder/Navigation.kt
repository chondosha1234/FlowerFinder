package com.chondosha.flowerfinder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chondosha.flowerfinder.ui.*

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "flower_list"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("flower_list") {
            FlowerListScreen(
                modifier = Modifier,
                onNavigateToDetail = { flowerId ->
                    navController.navigate("flower_detail/${flowerId}")
                },
                onNavigateToAbout = {
                    navController.navigate("about")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToNoMatch = {
                    navController.navigate("no_match")
                }
            )
        }
        composable(
            "flower_detail/{flowerEntryId}",
            arguments = listOf(navArgument("flowerEntryId") {type = NavType.StringType} )
        ) { backStackEntry ->
            FlowerDetailScreen(
                modifier = Modifier,
                flowerId = backStackEntry.arguments?.getString("flowerEntryId"),
                onNavigateToList = {
                    navController.navigate("flower_list")
                },
                onNavigateToWiki = { url ->
                    navController.navigate("wiki/${url}")
                }
            )
        }
        composable(
            "no_match"
        ) {
            NoMatchScreen(
                onNavigateBack = {
                    navController.navigate("flower_list")
                }
            )
        }
        composable(
            "wiki/{url}",
            arguments = listOf(navArgument("url") {type = NavType.StringType} ),
        ) { backStackEntry ->
            FlowerWiki(
                url = backStackEntry.arguments?.getString("url")
            )
        }
        composable(
            "about"
        ) {
            AboutScreen()
        }
        composable(
            "settings"
        ) {
            SettingsScreen()
        }
    }
}