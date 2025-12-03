package com.proyecto.recipeapp.ui.extras

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.models.Category

@Composable
fun CategoryItem(
    category: Category,
    navigateTo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = navigateTo,
        shape = CircleShape
            .copy(
                topEnd = CornerSize(15.dp),
                bottomEnd = CornerSize(15.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(category.strCategoryThumb)
                        .crossfade(true)////////////
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Meal Category",
                    loading = {
                        LoadingImage()
                    },
                    error = {
                        Icon(
                            painter = painterResource(R.drawable.ic_broken_image),
                            contentDescription = "Error al cargar imagen",
                            tint = Color.Gray
                        )
                    }
                )
            }

            Box(/////////////////
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.strCategory,
                    modifier = Modifier,
                )
            }

        }
    }
}