package com.proyecto.recipeapp.ui


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.ui.navigation.RecipeNavHost
import com.proyecto.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun RecipeApp(
    navController: NavHostController = rememberNavController(),
) {
    RecipeNavHost(
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    //scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(title,
               color = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}
