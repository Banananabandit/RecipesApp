package com.example.recipesapp.presentation.list

import com.example.recipesapp.domain.Recipe

data class RecipeScreenState(
    val recipes: List<Recipe>,
    val isLoading: Boolean,
    val error: String? = null
)
