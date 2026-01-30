package com.proyecto.recipeapp.ui.categoryMeals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.local.entities.MealEntity
import com.proyecto.recipeapp.ui.AppViewModelProvider
import com.proyecto.recipeapp.ui.RecipeTopAppBar
import com.proyecto.recipeapp.ui.categoryMeals.CategoryMealsViewModel.CategoryMealsUiState
import com.proyecto.recipeapp.ui.detail.DetailDestination
import com.proyecto.recipeapp.ui.extras.LoadingScreen
import com.proyecto.recipeapp.ui.extras.MealItem
import com.proyecto.recipeapp.ui.navigation.NavigationDestination

object CategoryMealsDestination : NavigationDestination {
    override val route = "CategoryMeals/{category}"
    override val titleRes = 0
    const val categoryArg = "category"
}

@Composable
fun CategoryMealsScreen(
    navController: NavHostController,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryMealsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.categoryMealsUiState.collectAsState()
    Scaffold(
        topBar = {
            RecipeTopAppBar(
                title = viewModel.categoryStr,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        when (uiState.value) {
            CategoryMealsUiState.Blank -> Box(
                modifier = modifier.padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_meals_found))
            }

            CategoryMealsUiState.Loading -> LoadingScreen(modifier.padding(innerPadding))
            is CategoryMealsUiState.Success -> CategoryMealsSuccess(
                modifier = modifier.padding(innerPadding),
                mealList = (uiState.value as CategoryMealsUiState.Success).meals,
                navController = navController
            )
        }
    }
}

@Composable
fun CategoryMealsSuccess(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mealList: List<MealEntity>
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            items(items = mealList) { meal ->
                MealItem(
                    meal = meal,
                    navigateTo = {
                        navController.navigate(
                            "${DetailDestination.route.substringBefore("/")}/${meal.id}"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}