package com.proyecto.recipeapp.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.local.entities.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: MealRepository) : ViewModel() {
    sealed interface CategoryUiState {
        object Loading : CategoryUiState
        object Error : CategoryUiState
        object Blank : CategoryUiState
        data class Success(val categories: List<CategoryEntity>) : CategoryUiState
    }

    // Estado para errores de red/sincronización
    private val _networkErrorState = MutableStateFlow<CategoryUiState?>(null)

    // COMBINAMOS la DB y el estado de red para crear el estado de la UI
    val categoryUiState: StateFlow<CategoryUiState> = combine(
        repository.getCategoriesStream(),
        _networkErrorState
    ) { categories, networkError ->
        if (categories.isEmpty()) {
            // Si no hay datos, mostramos el error de red o Loading
            networkError ?: CategoryUiState.Loading
        } else {
            // Si hay datos, siempre mostramos Success
            CategoryUiState.Success(categories)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CategoryUiState.Loading
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
                    _networkErrorState.value = CategoryUiState.Blank
                } else {
                    _networkErrorState.value = CategoryUiState.Error
                }
            }
        }
    }
}
