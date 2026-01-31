package com.proyecto.recipeapp.ui.home


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.proyecto.recipeapp.data.models.chipList
import com.proyecto.recipeapp.ui.AppViewModelProvider
import com.proyecto.recipeapp.ui.RecipeTopAppBar
import com.proyecto.recipeapp.ui.detail.DetailDestination
import com.proyecto.recipeapp.ui.extras.ChipRenderer
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
    BackHandler(enabled = viewModel.onSearchFocus) {
        viewModel.changeOnSearchFocus(false)
    }

    Scaffold(
        topBar = {
            RecipeTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            HomeScreenTop(
                viewModel = viewModel,
                navController = navController
            )
            HomeScreenBody(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun HomeScreenTop(
    viewModel: HomeViewModel,
    navController: NavHostController
) {
    Column {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            viewModel = viewModel
        )
        if(!viewModel.onSearchFocus){
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(32.dp)
            ) {
                items(items = chipList) { chipItem ->
                    ChipRenderer(
                        chipItem = chipItem,
                        navController = navController,
                        viewModel = viewModel
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}

@Composable
fun HomeScreenBody(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navController: NavHostController
){
    if (viewModel.onSearchFocus) {
        val searchState = viewModel.onSearchState.collectAsState()
        if (searchState.value.isEmpty()) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.no_meals_found))
            }
        }
        else{
            HomeSuccess(
                mealList = searchState.value,
                navController = navController
            )
        }
    } else {
        val uiState = viewModel.homeUiState.collectAsState()
        when (uiState.value) {
            HomeUiState.Loading -> LoadingScreen(modifier = modifier)

            HomeUiState.Error -> ErrorScreen(
                modifier = modifier,
                retryAction = { viewModel.refreshMeals() }
            )

            is HomeUiState.Success -> HomeSuccess(
                mealList = (uiState.value as HomeUiState.Success).meals,
                navController = navController
            )
        }
    }
}

@Composable
fun HomeSuccess(
    mealList: List<MealEntity>,
    navController: NavHostController
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

