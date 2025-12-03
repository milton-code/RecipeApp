package com.proyecto.recipeapp.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.models.Category
import com.proyecto.recipeapp.data.models.Meal
import com.proyecto.recipeapp.ui.home.HomeViewModel.HomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class CategoryViewModel(private val repository: MealRepository): ViewModel() {
    sealed interface CategoryUiState{
        object Loading: CategoryUiState
        object Error: CategoryUiState
        data class Success(val categories: List<Category>?): CategoryUiState
    }

    private val _categoryUiState: MutableStateFlow<CategoryUiState> = MutableStateFlow(CategoryUiState.Loading)
    val categoryUiState = _categoryUiState.asStateFlow()

    fun getCategories() {
        _categoryUiState.value = CategoryUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoryList = repository.getCategories().categories
                _categoryUiState.value = CategoryUiState.Success(categoryList)
            } catch (e: IOException) {
                _categoryUiState.value = CategoryUiState.Error
            }
        }
    }

    /*fun getMealsByCategory(category: String) {
        _categoryUiState.value = CategoryUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val mealList = repository.getMealsByCategory(category).meals
                _categoryUiState.value = CategoryUiState.SuccessMeal(category,mealList)
            }
            catch (e: IOException) {
                _categoryUiState.value = CategoryUiState.Error
            }
        }
    }*/

}