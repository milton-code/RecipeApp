package com.proyecto.recipeapp.ui.addRecipe

import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.ui.AppViewModelProvider
import com.proyecto.recipeapp.ui.RecipeTopAppBar
import com.proyecto.recipeapp.ui.navigation.NavigationDestination

object AddRecipeDestination : NavigationDestination {
    override val route = "MyRecipes"
    override val titleRes = R.string.add_recipe
}

@Composable
fun AddRecipeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AddRecipeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RecipeTopAppBar(
                title = stringResource(AddRecipeDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        AddRecipeBody(
            modifier = modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun AddRecipeBody(
    modifier: Modifier = Modifier,
    viewModel: AddRecipeViewModel
) {
    val context = LocalContext.current
    val uiState = viewModel.mealForm.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(all = 16.dp)
    ) {
        OutlinedTextField(
            value = uiState.value.mealName,
            onValueChange = { viewModel.updateMealName(it) },
            label = { Text(text = "Nombre de la receta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        CategoryDropdown(
            modifier = Modifier.fillMaxWidth(),
            dropDownMenuText = uiState.value.mealCategory,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        OutlinedTextField(
            value = uiState.value.mealInstructions,
            onValueChange = { viewModel.updateMealInstructions(it) },
            label = { Text(text = "Instrucciones de la receta") },
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(rememberScrollState(), Orientation.Vertical),
            minLines = 10,
            maxLines = 10
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    viewModel.addRecipe()
                    Toast.makeText(context,"Receta agregada",Toast.LENGTH_SHORT).show()
                },
            ) {
                Text(text = "Add recipe")
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    dropDownMenuText: String,
    viewModel: AddRecipeViewModel
) {
    val dropdownMenuState = viewModel.dropdownMenuState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }
    //var textFieldState by remember { mutableStateOf("Seleccione una categoría") }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = dropDownMenuText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            //colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            when (dropdownMenuState.value) {
                DropdownMenuState.Loading -> {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Cargando categorías...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {},
                    )
                }
                DropdownMenuState.Error -> {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Error al cargar categorías",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {},
                    )
                }
                DropdownMenuState.Blank -> {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "No hay categorías disponibles",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {},
                    )
                }
                is DropdownMenuState.Success -> {
                    val categories = (dropdownMenuState.value as DropdownMenuState.Success).categories.map { it.strCategory }
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    category,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                viewModel.updateMealCategory(category)
                                isExpanded = false
                            },
                        )
                    }
                }
            }
        }
    }


}

/*@Preview(showBackground = true)
@Composable
fun DropdownTestPreview() {
    CategoryDropdown(modifier = Modifier.fillMaxSize())
}*/

