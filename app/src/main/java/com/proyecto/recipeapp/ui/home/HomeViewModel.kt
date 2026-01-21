package com.proyecto.recipeapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.local.entities.MealEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(private val repository: MealRepository) : ViewModel() {
    sealed interface HomeUiState {
        object Loading : HomeUiState
        object Error : HomeUiState
        object Blank : HomeUiState
        data class Success(val meals: List<MealEntity>) : HomeUiState
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> = _searchQuery
        .debounce(300L) // Espera 300ms de silencio antes de buscar
        .flatMapLatest { query ->
            if (query.isBlank()) {
                // Si la búsqueda está vacía, muestra la lista inicial desde la base de datos
                repository.getMealsStream().map { meals ->
                    if (meals.isEmpty()) {
                        HomeUiState.Loading // Muestra carga mientras se realiza la sincronización inicial
                    } else {
                        HomeUiState.Success(meals)
                    }
                }
            } else {
                // Si hay una búsqueda, realiza una llamada de red
                flow {
                    val searchResult = repository.getMealsByName(query).meals ?: emptyList()
                    if(searchResult.isEmpty()){
                        emit(HomeUiState.Blank)
                    }
                    else {
                        val mealEntities = searchResult.map{
                            MealEntity(
                                idMeal = it.idMeal,
                                strMeal = it.strMeal,
                                strCategory = it.strCategory,
                                strInstructions = it.strInstructions,
                                strMealThumb = it.strMealThumb
                            )
                        }
                        emit(HomeUiState.Success(mealEntities))
                    }
                }
                .catch { emit(HomeUiState.Error) } // Captura errores de red
                .flowOn(Dispatchers.IO) // Ejecuta la llamada de red en un hilo de IO
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    var isFocused by mutableStateOf(false)
        private set

    init {
        initialSync()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun changeFocus(focus: Boolean) {
        isFocused = focus
    }

    private fun initialSync() {
        viewModelScope.launch {
            val meals = repository.getMealsStream().firstOrNull() ?: emptyList()
            if (meals.isEmpty()) {
                refreshMeals()
            }
        }
    }

    fun refreshMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.refreshMeals()
            } catch (e: Exception) {
                // El estado de error se gestionará automáticamente por el `catch` en `flatMapLatest`
                // si la base de datos sigue vacía y la red falla.
            }
        }
    }
}
