package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RecipesViewModel(): ViewModel() {
    val state = mutableStateOf(dummyListRecipes)

    fun toggleFavorite(id: Int) {
        val recipes = state.value.toMutableList()
        val recipeIndex = recipes.indexOfFirst { it.id == id }
        val recipe = recipes[recipeIndex]
        recipes[recipeIndex] = recipe.copy(isFavourite = !recipe.isFavourite)
        state.value = recipes
    }
}