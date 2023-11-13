package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class RecipesViewModel(private val stateHandle: SavedStateHandle): ViewModel() {

    private var restInterface: RecipesApiService
    val state = mutableStateOf(emptyList<Recipe>())
    private val errorHandler = CoroutineExceptionHandler{ _, exception ->
        exception.printStackTrace()
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://recipesapp-7cae7-default-rtdb.firebaseio.com/")
            .build()

        restInterface = retrofit.create(
            RecipesApiService::class.java
        )
        getRecipes()
    }

    private suspend fun getRemoteRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            restInterface.getRecipes()
        }
    }

    private fun getRecipes() {
        viewModelScope.launch(errorHandler) {
            val recipes = getRemoteRecipes()
                state.value = recipes.restoreSelections()
        }
    }
    private fun storeSelection(item: Recipe) {
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty()
            .toMutableList()

        if (item.isFavourite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }

    private fun List<Recipe>.restoreSelections(): List<Recipe> {
        stateHandle.get<List<Int>?>(FAVORITES)?.let { selectedIds ->
            val recipesMap = this.associateBy { it.id }
            selectedIds.forEach { id ->
                recipesMap[id]?.isFavourite = true
            }
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
        state.value = recipes
    }

    companion object {
        const val FAVORITES = "favorites"
    }
}
