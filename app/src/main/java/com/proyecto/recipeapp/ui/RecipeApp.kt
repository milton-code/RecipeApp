package com.proyecto.recipeapp.ui


import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.ui.navigation.RecipeNavHost

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
    showFavoriteIcon: Boolean = false,
    //scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(title)
        },
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (showFavoriteIcon) {
                var enabled by remember { mutableStateOf(false) }
                IconButton(onClick = {
                    enabled = !enabled
                    val message = if (enabled) {
                        "Agregado a favoritos"
                    } else {
                        "Quitado de favoritos"
                    }
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                }) {
                    if (!enabled) {
                        Icon(
                            imageVector = Icons.Default.StarBorder,
                            modifier = Modifier.fillMaxSize(0.75f),
                            tint = Color.Gray,
                            contentDescription = stringResource(R.string.favorites_button)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Star,
                            modifier = Modifier.fillMaxSize(0.75f),
                            tint = Color(0xFFE1C941),
                            contentDescription = stringResource(R.string.favorites_button)
                        )
                    }
                }
            }
        }

    )
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
  RecipeTopAppBar(
      title = "Recipe App",
      canNavigateBack = true,
      navigateUp = {}
  )
}