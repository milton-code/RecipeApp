package com.proyecto.recipeapp.ui.addRecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.local.entities.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MealForm(
    val mealName: String = "",
    val mealCategory: String = "Seleccione una categoría",
    val mealImage: String = "",
    val mealInstructions: String = "",
)

sealed interface DropdownMenuState {
    object Loading : DropdownMenuState
    object Error : DropdownMenuState
    object Blank : DropdownMenuState
    data class Success(val categories: List<CategoryEntity>) : DropdownMenuState
}

class AddRecipeViewModel(private val repository: MealRepository): ViewModel() {
    private val _mealForm = MutableStateFlow(MealForm())
    val mealForm: StateFlow<MealForm> = _mealForm.asStateFlow()
    private val _networkErrorState = MutableStateFlow<DropdownMenuState?>(null)

    val dropdownMenuState: StateFlow<DropdownMenuState> =
        combine(repository.getCategoriesStream(), _networkErrorState)
        { categories, networkError ->
            if(categories.isEmpty()){
                networkError ?: DropdownMenuState.Loading
            } else {
                DropdownMenuState.Success(categories)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DropdownMenuState.Loading
        )

    init {
        initialSync()
    }
    private fun initialSync() {
        viewModelScope.launch {
            val categories = repository.getCategoriesStream().firstOrNull() ?: emptyList()
            if (categories.isEmpty()) {
                refreshCategories()
            }
        }
    }

    fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _networkErrorState.value = null // Limpiamos errores previos
                repository.refreshCategories()
            } catch (e: Exception) {
                if (e.message == "isBlank") {
                    _networkErrorState.value = DropdownMenuState.Blank
                } else {
                    _networkErrorState.value = DropdownMenuState.Error
                }
            }
        }
    }

    fun addRecipe(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecipe(_mealForm.value)
        }
    }

    fun updateMealName(mealName: String) {
        _mealForm.value = _mealForm.value.copy(mealName = mealName)
    }

    fun updateMealCategory(mealCategory: String) {
        _mealForm.value = _mealForm.value.copy(mealCategory = mealCategory)
    }

    fun updateMealImage(mealImage: String) {
        _mealForm.value = _mealForm.value.copy(mealImage = mealImage)
    }

    fun updateMealInstructions(mealInstructions: String) {
        _mealForm.value = _mealForm.value.copy(mealInstructions = mealInstructions)
    }
}
