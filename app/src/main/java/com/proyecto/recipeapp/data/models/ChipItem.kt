package com.proyecto.recipeapp.data.models


import androidx.navigation.NavHostController
import com.proyecto.recipeapp.ui.category.CategoryDestination

sealed interface ChipItem {
    data class Category(val onClick: (NavHostController) -> Unit) : ChipItem
    data class Favorites(val onClick: () -> Unit) : ChipItem
    data class Country(val onClick: () -> Unit) : ChipItem
    data class MyRecipes(val onClick: () -> Unit) : ChipItem
}

val chipList = listOf(
    ChipItem.Category { it.navigate(CategoryDestination.route) },
    ChipItem.MyRecipes { },
    ChipItem.Country { },
    ChipItem.Favorites { }
)




