package com.proyecto.recipeapp.data.models


import androidx.navigation.NavHostController
import com.proyecto.recipeapp.ui.addRecipe.AddRecipeDestination
import com.proyecto.recipeapp.ui.category.CategoryDestination

sealed interface ChipItem {
    data class Category(val onClick: (NavHostController) -> Unit) : ChipItem
    data class AddRecipe(val onClick: (NavHostController) -> Unit) : ChipItem
    object Favorites : ChipItem
    object MyRecipes : ChipItem

}

val chipList = listOf(
    ChipItem.Category { it.navigate(CategoryDestination.route) },
    ChipItem.AddRecipe { it.navigate(AddRecipeDestination.route) },
    ChipItem.MyRecipes,
    ChipItem.Favorites,
)




