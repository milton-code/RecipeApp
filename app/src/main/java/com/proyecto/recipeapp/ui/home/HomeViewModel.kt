package com.proyecto.recipeapp.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.recipeapp.data.MealRepository
import com.proyecto.recipeapp.data.local.entities.MealEntity
import com.proyecto.recipeapp.data.models.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class HomeViewModel(private val repository: MealRepository): ViewModel() {
    sealed interface HomeUiState{
        object Loading: HomeUiState
        object Error: HomeUiState
        data class Success(val meals: List<MealEntity>): HomeUiState
    }
    var onSearchFocus by mutableStateOf(false)
        private set

    //private val _homeUiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    private val errorState = MutableStateFlow<Boolean>(false)
    val homeUiState: StateFlow<HomeUiState> =
        combine(errorState, repository.getMealsStream()) { error, meals ->
            if (error) {
                HomeUiState.Error
            } else {
                if (meals.isEmpty()) {
                    HomeUiState.Loading
                } else {
                    HomeUiState.Success(meals)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    init {
        //onSearchQueryChange()
        initialSync()
    }

    var isFocused by mutableStateOf(false)
        private set
    val searchQuery = MutableStateFlow("")


    init {
        onSearchQueryChange()
    }
    @OptIn(FlowPreview::class)
    fun onSearchQueryChange() {
        viewModelScope.launch {
            searchQuery
                .debounce(500L)
                .collectLatest { name ->
                    getMealsByName(name)
                }
        }
    }

    fun searchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun changeOnSearchFocus(focus: Boolean) {
        onSearchFocus.value = focus
    }


    fun getMealsByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                val mealsList: List<Meal>? = repository.getMealsByName(encodedName).meals
                _homeUiState.value = HomeUiState.Success(mealsList)
            } catch (e: IOException) {
                _homeUiState.value = HomeUiState.Error
            }
        }
    }

}