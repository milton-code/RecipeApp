package com.proyecto.recipeapp.ui.addRecipe

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
            viewModel = viewModel,
            navigateUp = navigateBack
        )
    }
}

@Composable
fun AddRecipeBody(
    modifier: Modifier = Modifier,
    viewModel: AddRecipeViewModel,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    val uiState = viewModel.mealForm.collectAsState()

    // Selector de imágenes
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.updateMealImage(uri.toString()) }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding()
    ) {
        OutlinedTextField(
            value = uiState.value.mealName,
            onValueChange = { viewModel.updateMealName(it) },
            label = { Text(text = stringResource(R.string.recipe_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        CategoryDropdown(
            modifier = Modifier.fillMaxWidth(),
            dropDownMenuText = uiState.value.mealCategory,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        // Botón para seleccionar imagen y vista previa //
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (uiState.value.mealImage.isNotEmpty()) {
                AsyncImage(
                    model = uiState.value.mealImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (uiState.value.mealImage.isEmpty()) stringResource(R.string.image_add) else stringResource(
                    R.string.image_change
                ))
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        OutlinedTextField(
            value = uiState.value.mealInstructions,
            onValueChange = { viewModel.updateMealInstructions(it) },
            label = { Text(text = stringResource(R.string.recipe_instructions)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            maxLines = 10
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    if (uiState.value.mealName.isBlank() || uiState.value.mealCategory == context.getString(
                            R.string.select_category
                        )) {
                        Toast.makeText(context,
                            context.getString(R.string.required_fields_error), Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addRecipe()
                        navigateUp.invoke()
                        Toast.makeText(context,
                            context.getString(R.string.recipe_added), Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.add_recipe_button))
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
            label = { Text(stringResource(R.string.category)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            when (dropdownMenuState.value) {
                DropdownMenuState.Loading -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.categories_loading), style = MaterialTheme.typography.bodyLarge) },
                        onClick = {},
                    )
                }
                DropdownMenuState.Error -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.categories_loading_error), style = MaterialTheme.typography.bodyLarge) },
                        onClick = {},
                    )
                }
                DropdownMenuState.Blank -> {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.categories_unavailable), style = MaterialTheme.typography.bodyLarge) },
                        onClick = {},
                    )
                }
                is DropdownMenuState.Success -> {
                    val categories = (dropdownMenuState.value as DropdownMenuState.Success).categories
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.strCategory, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                viewModel.updateMealCategory(category.strCategory)
                                isExpanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}
