package com.proyecto.recipeapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.proyecto.recipeapp.ui.addRecipe.AddRecipeDestination
import com.proyecto.recipeapp.ui.addRecipe.AddRecipeScreen
import com.proyecto.recipeapp.ui.category.CategoryDestination
import com.proyecto.recipeapp.ui.category.CategoryScreen
import com.proyecto.recipeapp.ui.categoryMeals.CategoryMealsDestination
import com.proyecto.recipeapp.ui.categoryMeals.CategoryMealsScreen
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
        composable(route = HomeDestination.route) {
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
        composable(route = CategoryDestination.route) {
            CategoryScreen(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = CategoryMealsDestination.route,// Usa la ruta con el placeholder
            arguments = listOf(navArgument(CategoryMealsDestination.categoryArg) {
                type = NavType.StringType// ¡Muy importante! Define el tipo
             }
            )
        ) {
            CategoryMealsScreen(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                navigateBack = { navController.navigateUp() },
            )
        }
        composable(route = AddRecipeDestination.route) {
            AddRecipeScreen(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                navigateBack = {navController.navigateUp()}
            )
        }
    }
}
