package com.proyecto.recipeapp.ui.extras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.models.ChipItem
import com.proyecto.recipeapp.ui.home.HomeViewModel

@Composable
fun ChipRenderer(
    chipItem: ChipItem,
    navController: NavHostController,
    viewModel: HomeViewModel
) {

    when (chipItem) {
        is ChipItem.Category -> AssistChip(
            label = { Text(stringResource(R.string.categories)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Category,
                    tint = Color.Gray,
                    contentDescription = null
                )
            },
            onClick = { chipItem.onClick(navController) }
        )

        is ChipItem.MyRecipes -> {
            val selected = viewModel.myRecipesFilterState.collectAsState()
            FilterChip(
            label = { Text(stringResource(R.string.my_recipes)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null
                )
            },
            onClick = {
                viewModel.toggleMyRecipesFilter()
            },
            selected = selected.value,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF2D7230),
                selectedLabelColor = Color.White,
                iconColor = Color.Gray,
                selectedLeadingIconColor = Color.White
            )
        )
    }

        is ChipItem.Favorites -> {
            val selected = viewModel.favoriteFilterState.collectAsState()
            FilterChip(
                label = { Text(stringResource(R.string.favorites)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null
                    )
                },
                onClick = {
                    viewModel.toggleFavoriteFilter()
                },
                selected = selected.value,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2D7230),
                    selectedLabelColor = Color.White,
                    iconColor = Color.Gray,
                    selectedLeadingIconColor = Color.White
                )
            )
        }

        is ChipItem.AddRecipe -> AssistChip(
            label = { Text(stringResource(R.string.add_recipe)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.Gray,
                    contentDescription = null
                )
            },
            onClick = { chipItem.onClick(navController) }
        )
    }
}
