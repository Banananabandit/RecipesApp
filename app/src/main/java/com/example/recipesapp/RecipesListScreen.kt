package com.example.recipesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipesapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesListScreen() {
    val viewModel: RecipesViewModel = viewModel()
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        )
    ){
        items(viewModel.state.value) { recipe ->
            RecipeListItem(recipe) { id ->
                viewModel.toggleFavorite(id)
            }
        }
    }

}

@Composable
fun RecipeListItem(item: Recipe,
                   onClick: (id: Int) -> Unit) {
    val icon = if (item.isFavourite)
        Icons.Filled.Favorite
    else
        Icons.Filled.FavoriteBorder

    Card(elevation = CardDefaults.cardElevation(),
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight())
    {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)) {
            RecipeIcon(
                Icons.Filled.Delete,
                Modifier.weight(0.15f))
            RecipeDetails(
                item.title,
                item.description,
                Modifier.weight(0.7f))
            RecipeIcon(icon, Modifier.weight(0.15f)) {
                onClick(item.id)
            }
        }
    }
}


@Composable
private fun RecipeDetails(name: String, description: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = name,
            style = MaterialTheme.typography.headlineSmall)
        Text(text = description,
            style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun RecipeIcon(icon: ImageVector, modifier: Modifier, onClick: () -> Unit = {}) {
    Image(imageVector = icon,
        contentDescription = "Recipe icon",
        modifier = modifier
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() })
}

@Preview
@Composable
fun DefaultPreview() {
    RecipesAppTheme {
        RecipesListScreen()
    }
}