package com.proyecto.recipeapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.local.entities.MealEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: MealRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel(){
    val mealId: Int = checkNotNull(savedStateHandle[DetailDestination.mealIdArg])
    sealed interface DetailUiState{
        object Loading: DetailUiState
        object Error: DetailUiState
        data class Success(val meal: MealEntity): DetailUiState
    }
    val detailUiState: StateFlow<DetailUiState> = repository
        .getMealById(mealId).map { meal ->
            if (meal != null) {
                DetailUiState.Success(meal)
            } else {
                DetailUiState.Loading
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailUiState.Loading
        )

    fun updateMealFavorite(meal: MealEntity){
        val updatedMeal = meal.copy(isFavorite = !meal.isFavorite)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMeal(updatedMeal)
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMeal(meal)
        }
    }
}