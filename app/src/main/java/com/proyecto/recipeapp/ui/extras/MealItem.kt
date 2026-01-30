package com.proyecto.recipeapp.ui.extras

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.proyecto.recipeapp.R
import com.proyecto.recipeapp.data.local.entities.MealEntity


@Composable
fun LoadingImage() {
    Box(modifier = Modifier.padding(25.dp),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = Color.Gray
        )
    }

}
@Composable
fun MealItem(
    meal: MealEntity,
    navigateTo: () -> Unit = {},
) {
    Card(
        onClick = navigateTo,
        shape = CircleShape
            .copy(
                topEnd = CornerSize(15.dp),
                bottomEnd = CornerSize(15.dp)
            ),
        modifier = Modifier
    ) {
        Row (modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()){
            Box(modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                ) {
                SubcomposeAsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(meal.strMealThumb)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(R.string.recipe),
                loading = {
                    LoadingImage()
                },
                    error = {
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                painter = painterResource(R.drawable.ic_broken_image),
                                contentDescription = stringResource(R.string.loading_image_error),
                                tint = Color.Gray
                            )
                        }
                    }
            )
            }

            Box(modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxSize(),
                contentAlignment = Alignment.Center){
                Column()
                {
                    Text(
                        text = meal.strMeal,
                        modifier = Modifier,
                    )
                }
            }

        }
    }
}
