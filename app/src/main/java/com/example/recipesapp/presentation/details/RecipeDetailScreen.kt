package com.example.recipesapp.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipesapp.presentation.list.RecipeDetails
import com.example.recipesapp.presentation.list.RecipeIcon

@Composable
fun RecipeDetailScreen() {
    val viewModel: RecipeDetailsViewModel = viewModel()
    val item = viewModel.state.value
    
    if (item != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            RecipeIcon(
                icon = Icons.Filled.Delete, 
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp))
            RecipeDetails(
                name = item.title, 
                description = item.description, 
                modifier = Modifier.padding(bottom = 32.dp),
                Alignment.CenterHorizontally)
            Text(text = "List of ingredients will go here")
        }
    }
}