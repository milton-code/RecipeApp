package com.proyecto.recipeapp.ui.home


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.models.Meal
import com.proyecto.recipeapp.ui.AppViewModelProvider
import com.proyecto.recipeapp.ui.RecipeTopAppBar
import com.proyecto.recipeapp.ui.detail.DetailDestination
import com.proyecto.recipeapp.ui.extras.ErrorScreen
import com.proyecto.recipeapp.ui.extras.LoadingScreen
import com.proyecto.recipeapp.ui.extras.MealItem
import com.proyecto.recipeapp.ui.extras.SearchBar
import com.proyecto.recipeapp.ui.home.HomeViewModel.HomeUiState
import com.proyecto.recipeapp.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.homeUiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getMealsByName(viewModel.searchQuery.value)
    }

    Scaffold(
        topBar = {
            RecipeTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        when (uiState.value) {
            HomeUiState.Loading -> LoadingScreen(
                modifier
                    .padding(innerPadding)
            )

            HomeUiState.Error -> ErrorScreen(
                modifier = modifier
                    .padding(innerPadding),
                retryAction = { viewModel.getMealsByName("") }
            )

            is HomeUiState.Success -> HomeSuccess(
                mealList = (uiState.value as HomeUiState.Success).meals,
                navController = navController,
                viewModel = viewModel,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}


@Composable
fun HomeSuccess(
    mealList: List<Meal>?,
    navController: NavHostController,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (mealList != null) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                items(items = mealList) { meal ->
                    MealItem(
                        meal = meal,
                        navigateTo = {
                            navController.navigate(
                                "${DetailDestination.route.substringBefore("/")}/${meal.idMeal}"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_meals_found),
                    color = Color.Gray
                )
            }
        }
    }
}

