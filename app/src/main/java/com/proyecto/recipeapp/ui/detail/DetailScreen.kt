package com.proyecto.recipeapp.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.models.Meal
import com.proyecto.recipeapp.ui.AppViewModelProvider
import com.proyecto.recipeapp.ui.RecipeTopAppBar
import com.proyecto.recipeapp.ui.detail.DetailViewModel.DetailUiState
import com.proyecto.recipeapp.ui.extras.ErrorScreen
import com.proyecto.recipeapp.ui.extras.LoadingImage
import com.proyecto.recipeapp.ui.extras.LoadingScreen
import com.proyecto.recipeapp.ui.navigation.NavigationDestination

object DetailDestination : NavigationDestination {
    override val route: String = "Detail/{mealId}"
    override val titleRes = 0
    const val mealIdArg = "mealId"
}

@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.detailUiState.collectAsState()
    LaunchedEffect(key1 = viewModel.mealId) {
        viewModel.getMealById(viewModel.mealId)
    }
    when (uiState.value) {
        DetailUiState.Loading -> LoadingScreen(modifier)
        DetailUiState.Error -> ErrorScreen(
            modifier,
            retryAction = { viewModel.getMealById(viewModel.mealId) }
        )

        is DetailUiState.Success -> DetailSuccess(
            meal = (uiState.value as DetailUiState.Success).meal,
            navigateBack = navigateBack,
            modifier = modifier
        )
    }
}

@Composable
fun DetailSuccess(
    meal: Meal,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            RecipeTopAppBar(
                title = meal.strMeal,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        MealDetail(
            imageUrl = meal.strMealThumb,
            instructions = meal.strInstructions,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MealDetail(
    imageUrl: String,
    instructions: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.clip(shape = RoundedCornerShape(15.dp))) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.meal_picture),
                modifier = Modifier.fillMaxWidth(0.8f),
                contentScale = ContentScale.Fit,
                loading = {
                    LoadingImage()
                },
                error = {
                    Icon(
                        painter = painterResource(R.drawable.ic_broken_image),
                        contentDescription = stringResource(R.string.image_not_found),
                        tint = Color.Gray
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = instructions,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        )

    }
}