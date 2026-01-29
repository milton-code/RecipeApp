package com.proyecto.recipeapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.proyecto.recipeapp.RecipeApplication
import com.proyecto.recipeapp.ui.addRecipe.AddRecipeViewModel
import com.proyecto.recipeapp.ui.category.CategoryViewModel
import com.proyecto.recipeapp.ui.categoryMeals.CategoryMealsViewModel
import com.proyecto.recipeapp.ui.detail.DetailViewModel
import com.proyecto.recipeapp.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                recipeApplication().container.mealRepository//this.
            )
        }
        initializer {
            DetailViewModel(
                recipeApplication().container.mealRepository,
                this.createSavedStateHandle(),
            )
        }
        initializer {
            CategoryViewModel(
                recipeApplication().container.mealRepository
            )
        }
        initializer {
            CategoryMealsViewModel(
                recipeApplication().container.mealRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            AddRecipeViewModel(
                recipeApplication().container.mealRepository,
                recipeApplication()
            )
        }
    }
}

fun CreationExtras.recipeApplication(): RecipeApplication {
    return (this[AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)
}