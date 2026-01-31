package com.proyecto.recipeapp.ui.extras

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.ui.home.HomeViewModel

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val isFocused = viewModel.onSearchFocus
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (isFocused) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                        viewModel.changeOnSearchFocus(false)
                        focusManager.clearFocus()
                        viewModel.searchQueryChange("")
                    }
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(8.dp))
                ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = if (isFocused) Color.Gray else Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp)
            )
            SearchTextField(
                modifier = Modifier.fillMaxWidth(),
                text = viewModel.searchQuery.collectAsState().value,
                onTextChange = {
                    viewModel.searchQueryChange(it)
                },
                onFocusChange = { focusState ->
                    viewModel.changeOnSearchFocus(focusState)
                }
            )
        }
    }
}

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        singleLine = true,
        modifier = modifier
            .onFocusChanged { focusState ->
                onFocusChange(focusState.isFocused)
            }
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_recipes),
                    color = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent
            ),
            contentPadding = PaddingValues(
                vertical = 4.dp,
                horizontal = 8.dp
            ),
            container = {}
        )
    }
}