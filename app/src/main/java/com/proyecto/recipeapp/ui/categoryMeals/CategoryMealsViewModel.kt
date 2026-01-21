package com.proyecto.recipeapp.ui.categoryMeals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.local.entities.MealEntity
import com.proyecto.recipeapp.data.models.Category
import com.proyecto.recipeapp.data.models.Meal
import com.proyecto.recipeapp.ui.detail.DetailDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class CategoryMealsViewModel(
    private val repository: MealRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val categoryStr: String = checkNotNull(savedStateHandle[CategoryMealsDestination.categoryArg])
    sealed interface CategoryMealsUiState{
        object Loading: CategoryMealsUiState
        object Error: CategoryMealsUiState
        data class Success(val meals: List<Meal>?): CategoryMealsUiState
    }

    private val _categoryMealsUiState: MutableStateFlow<CategoryMealsUiState> =
        MutableStateFlow(CategoryMealsUiState.Loading)
    val categoryMealsUiState = _categoryMealsUiState.asStateFlow()

    fun getMealsByCategory(strCategory: String) {
        _categoryMealsUiState.value = CategoryMealsUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val mealList = repository.getMealsByCategory(strCategory).meals
                _categoryMealsUiState.value = CategoryMealsUiState.Success(
                    meals = mealList
                )
            }
            catch (e: IOException) {
                _categoryMealsUiState.value = CategoryMealsUiState.Error
            }
        }
    }
}