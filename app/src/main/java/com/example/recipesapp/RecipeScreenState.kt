package com.example.recipesapp

data class RecipeScreenState(
    val recipes: List<Recipe>,
    val isLoading: Boolean,
    val error: String? = null
)
