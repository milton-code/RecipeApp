package com.proyecto.recipeapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.models.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel(
    private val repository: MealRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel(){

    val mealId: Int = checkNotNull(savedStateHandle[DetailDestination.mealIdArg])
    sealed interface DetailUiState{
        object Loading: DetailUiState
        object Error: DetailUiState
        data class Success(val meal: Meal): DetailUiState
    }
    private val _detailUiState: MutableStateFlow<DetailUiState> =
        MutableStateFlow(DetailUiState.Loading)
    val detailUiState = _detailUiState.asStateFlow()

    fun getMealById(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val meals: List<Meal>? = repository.getMealById(id).meals
                if (meals != null) {
                    _detailUiState.value = DetailUiState.Success(meals[0])
                } else {
                    _detailUiState.value = DetailUiState.Error
                }
            }
            catch (e: IOException) {
                _detailUiState.value = DetailUiState.Error
            }
        }
    }

}