package com.proyecto.recipeapp.ui.categoryMeals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.local.entities.MealEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CategoryMealsViewModel(
    private val repository: MealRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val categoryStr: String = checkNotNull(savedStateHandle[CategoryMealsDestination.categoryArg])
    sealed interface CategoryMealsUiState{
        object Loading: CategoryMealsUiState
        object Blank: CategoryMealsUiState
        data class Success(val meals: List<MealEntity>): CategoryMealsUiState
    }

    val categoryMealsUiState: StateFlow<CategoryMealsUiState> = repository
        .getMealsStream()
        .map{ meals ->
            val filteredMeals = meals.filter { it.strCategory == categoryStr }
            if (filteredMeals.isEmpty()) {
                CategoryMealsUiState.Blank
            } else
            CategoryMealsUiState.Success(filteredMeals)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CategoryMealsUiState.Loading
        )


}