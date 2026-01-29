package com.proyecto.recipeapp.ui.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.local.entities.CategoryEntity
import com.proyecto.recipeapp.ui.AppViewModelProvider
import com.proyecto.recipeapp.ui.RecipeTopAppBar
import com.proyecto.recipeapp.ui.category.CategoryViewModel.CategoryUiState
import com.proyecto.recipeapp.ui.categoryMeals.CategoryMealsDestination
import com.proyecto.recipeapp.ui.extras.CategoryItem
import com.proyecto.recipeapp.ui.extras.ErrorScreen
import com.proyecto.recipeapp.ui.extras.LoadingScreen
import com.proyecto.recipeapp.ui.navigation.NavigationDestination

object CategoryDestination : NavigationDestination {
    override val route = "Categories"
    override val titleRes = R.string.categories
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateBack: () -> Unit,
    viewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.categoryUiState.collectAsState()
    Scaffold(
        topBar = {
            RecipeTopAppBar(
                title = stringResource(CategoryDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        when (uiState.value) {
            CategoryUiState.Loading -> LoadingScreen(
                modifier
                    .padding(innerPadding)
            )

            CategoryUiState.Error -> ErrorScreen(
                modifier = modifier
                    .padding(innerPadding),
                retryAction = {
                    viewModel.refreshCategories()
                }
            )

            CategoryUiState.Blank -> BlankScreen(modifier.padding(innerPadding))

            is CategoryUiState.Success -> CategorySuccess(
                    categoryList = (uiState.value as CategoryUiState.Success).categories,
                    navController = navController,
                    modifier = modifier.padding(innerPadding)
                )

        }
    }
}


@Composable
fun CategorySuccess(
    categoryList: List<CategoryEntity>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                items(items = categoryList) { category ->
                    CategoryItem(
                        category = category,
                        navigateTo = {
                            navController.navigate(
                                "${
                                    CategoryMealsDestination.route.substringBefore(
                                        "/"
                                    )
                                }/${category.strCategory}"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }


@Composable
fun BlankScreen(modifier: Modifier = Modifier){
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No categories found",
            color = Color.Gray
        )
    }
}