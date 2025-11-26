package com.proyecto.recipeapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.proyecto.recipeapp.RecipeApplication
import com.proyecto.recipeapp.ui.detail.DetailViewModel
import com.proyecto.recipeapp.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                this.recipeApplication().container.mealRepository
            )
        }
        initializer {
            DetailViewModel(
                recipeApplication().container.mealRepository,
                this.createSavedStateHandle(),
            )
        }
    }
}

fun CreationExtras.recipeApplication(): RecipeApplication {
    return (this[AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)
}