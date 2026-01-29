package com.proyecto.recipeapp.ui.home

import android.util.Log
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.collections.emptyList

class HomeViewModel(private val repository: MealRepository): ViewModel() {
    var onSearchFocus by mutableStateOf(false)
        private set
    //----Search State----//
    val searchQuery = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val onSearchState: StateFlow<List<MealEntity>> = searchQuery
        .debounce(500)
        .flatMapLatest { query ->
            if(query.isEmpty()) {
                flow {
                    emit(emptyList())
                }
            }
            else repository.getMealsByName(query)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    fun searchQueryChange(query: String) {
        searchQuery.value = query
    }
    fun changeOnSearchFocus(focus: Boolean) {
        onSearchFocus = focus
    }

    //----Starter State----//
    sealed interface HomeUiState{
        object Loading: HomeUiState
        object Error: HomeUiState
        data class Success(val meals: List<MealEntity>): HomeUiState
    }
    private val errorState = MutableStateFlow(false)
    val favoriteFilterState = MutableStateFlow(false)
    val myRecipesFilterState = MutableStateFlow(false)
    val homeUiState: StateFlow<HomeUiState> =
        combine(
            errorState,
            repository.getMealsStream(),
            favoriteFilterState,
            myRecipesFilterState
        ) { error, meals, favoriteFilter, myRecipesFilter ->
            if (error) {
                HomeUiState.Error
            } else {
                if (meals.isEmpty()) {
                    HomeUiState.Loading
                } else {
                    if (favoriteFilter && myRecipesFilter) {
                        HomeUiState.Success((meals.filter { it.isFavorite })
                            .filter { it.isCustom })
                    } else if (favoriteFilter){
                        HomeUiState.Success(meals.filter { it.isFavorite })
                    } else if (myRecipesFilter) {
                        HomeUiState.Success(meals.filter { it.isCustom })
                    } else {
                        HomeUiState.Success(meals)
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    init {
        initialSync()
    }

    private fun initialSync() {
        viewModelScope.launch {
            val meals = repository.getMealsStream().firstOrNull()?: emptyList()
            if (meals.isEmpty()) {
                refreshMeals()
            }
        }
    }
    fun refreshMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.refreshMeals()
            } catch (e: IOException) {
                Log.e("HomeViewModel", "Error refreshing meals: ${e.message}")
                errorState.value = true
            }
        }
    }

    fun toggleFavoriteFilter() {
        favoriteFilterState.value = !favoriteFilterState.value
    }

    fun toggleMyRecipesFilter() {
        myRecipesFilterState.value = !myRecipesFilterState.value
    }
}
