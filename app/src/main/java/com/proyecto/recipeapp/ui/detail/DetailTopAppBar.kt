package com.proyecto.recipeapp.ui.detail

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.local.entities.MealEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    meal: MealEntity,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    viewModel: DetailViewModel
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(meal.strMeal,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            if(meal.isCustom) {
                IconButton(onClick = {
                    viewModel.deleteMeal(meal)
                    navigateUp.invoke()
                    Toast.makeText(context,
                        context.getString(R.string.recipe_deleted), Toast.LENGTH_SHORT).show()
                }){
                    Icon(
                        imageVector = Icons.Default.Delete,
                        modifier = Modifier.fillMaxSize(0.75f),
                        contentDescription = stringResource(R.string.delete_button)
                    )
                }
            }
            IconButton(onClick = {
                viewModel.updateMealFavorite(meal)
                if (meal.isFavorite) {
                    Toast.makeText(context,
                        context.getString(R.string.favorites_removed), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,
                        context.getString(R.string.favorites_added), Toast.LENGTH_SHORT).show()
                }
            }) {
                if (meal.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        modifier = Modifier.fillMaxSize(0.75f),
                        tint = Color(0xFFE1C941),
                        contentDescription = stringResource(R.string.favorites_button)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.StarBorder,
                        modifier = Modifier.fillMaxSize(0.75f),
                        contentDescription = stringResource(R.string.favorites_button)
                    )
                }
            }
        }
    )
}