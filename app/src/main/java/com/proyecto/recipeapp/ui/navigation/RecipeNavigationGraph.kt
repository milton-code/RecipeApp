package com.proyecto.recipeapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.proyecto.recipeapp.ui.detail.DetailDestination
import com.proyecto.recipeapp.ui.detail.DetailScreen
import com.proyecto.recipeapp.ui.home.HomeDestination
import com.proyecto.recipeapp.ui.home.HomeScreen


@Composable
fun RecipeNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
    ) {
        composable (route = HomeDestination.route) {
            HomeScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(
            route = DetailDestination.route,// Usa la ruta con el placeholder
            arguments = listOf(navArgument(DetailDestination.mealIdArg) {
                type = NavType.IntType// ¡Muy importante! Define el tipo
            })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getInt(DetailDestination.mealIdArg)
            DetailScreen(
                navigateBack = { navController.navigateUp() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
