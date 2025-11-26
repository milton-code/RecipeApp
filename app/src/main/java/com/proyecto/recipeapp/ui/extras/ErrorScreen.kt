package com.proyecto.recipeapp.ui.extras

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyecto.recipeapp.R

@Composable
fun ErrorScreen(modifier: Modifier = Modifier,
                retryAction: () -> Unit) {
        Column (
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(R.drawable.ic_connection_error),
                contentDescription = stringResource(R.string.connection_error)
            )
            Text(text = stringResource(R.string.connection_error),
                modifier = Modifier.padding(16.dp))
            Button(onClick = retryAction) {
                Text(text = "Retry")
            }
        }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    ErrorScreen(
        modifier = Modifier.fillMaxSize(),
        retryAction = {}
    )
}
