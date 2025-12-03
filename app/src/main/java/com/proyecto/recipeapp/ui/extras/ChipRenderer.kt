package com.proyecto.recipeapp.ui.extras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.models.ChipItem

@Composable
fun ChipRenderer(chipItem: ChipItem, navController: NavHostController) {
    when (chipItem) {
        is ChipItem.Category -> AssistChip(
            label = { Text(stringResource(R.string.categories))},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Category,
                    tint = Color.Gray,
                    contentDescription = null
                )
            },
            onClick =  { chipItem.onClick(navController) }
        )

        is ChipItem.Favorites -> FilterChip(
            label = { Text(stringResource(R.string.favorites))},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = Color.Gray,
                    contentDescription = null
                )
            },
            onClick = chipItem.onClick,
            selected = false
        )

        is ChipItem.Country -> {}
        is ChipItem.MyRecipes -> {}
    }
}