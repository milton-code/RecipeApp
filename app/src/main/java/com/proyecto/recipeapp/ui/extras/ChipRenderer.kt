package com.proyecto.recipeapp.ui.extras

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
    var selectedMyRecipes by remember { mutableStateOf(false) }
    var selectedFavorites by remember { mutableStateOf(false) }
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

        is ChipItem.MyRecipes -> FilterChip(
            label = { Text(stringResource(R.string.my_recipes)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null
                )
            },
            onClick = {
                selectedMyRecipes = !selectedMyRecipes
                      },
            selected = selectedMyRecipes,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF2D7230),
                selectedLabelColor = Color.White,
                iconColor = Color.Gray,
                selectedLeadingIconColor = Color.White
            )
        )

        is ChipItem.Favorites -> FilterChip(
            label = { Text(stringResource(R.string.favorites)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            },
            onClick = { selectedFavorites = !selectedFavorites},
            selected = selectedFavorites,
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF2D7230),
                selectedLabelColor = Color.White,
                iconColor = Color.Gray,
                selectedLeadingIconColor = Color.White
            )
        )

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

@Preview(showBackground = true)
@Composable
fun ChipPreview() {
    var selected by remember { mutableStateOf(false) }
    FilterChip(
        label = { Text(stringResource(R.string.my_recipes)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.RestaurantMenu,
                contentDescription = null
            )
        },
        onClick = { selected = !selected},
        selected = selected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF2D7230),
            selectedLabelColor = Color.White,
            iconColor = Color.Gray,
            selectedLeadingIconColor = Color.White
        )
    )
}