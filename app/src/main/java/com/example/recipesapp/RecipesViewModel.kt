package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class RecipesViewModel(
    private val stateHandle: SavedStateHandle): ViewModel() {

    val state = mutableStateOf(dummyListRecipes.restoreSelections())
    private fun storeSelection(item: Recipe) {
        println("STORE HAS BEEN CALLED!")
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty()
            .toMutableList()

        if (item.isFavourite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
        println(stateHandle[FAVORITES])
    }

    private fun List<Recipe>.restoreSelections(): List<Recipe> {
        println("RESTORE!!!!!")
        println(stateHandle[FAVORITES])
        stateHandle.get<List<Int>?>(FAVORITES)?.let { selectedIds ->
            val recipesMap = this.associateBy { it.id }
            selectedIds.forEach { id ->
                recipesMap[id]?.isFavourite = true
            }
            println("HERE IS THE RESTORED LIST!!${recipesMap.toString()}")
            return recipesMap.values.toList()
        }
        return this
    }

    fun toggleFavorite(id: Int) {
        val recipes = state.value.toMutableList()
        val recipeIndex = recipes.indexOfFirst { it.id == id }
        val recipe = recipes[recipeIndex]
        recipes[recipeIndex] = recipe.copy(isFavourite = !recipe.isFavourite)
        storeSelection(recipes[recipeIndex])
        println("ADDED TTO RESTORATION!!!!!!!")
        state.value = recipes
    }
    companion object {
        const val FAVORITES = "favorites"
    }
}