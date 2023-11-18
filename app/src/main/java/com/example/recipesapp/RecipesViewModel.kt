package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import java.net.ConnectException
import java.net.UnknownHostException

class RecipesViewModel(private val stateHandle: SavedStateHandle): ViewModel() {

    private val restInterface: RecipesApiService
    private var recipesDao= RecipesDb.getDaoInstance(RecipesApplication.getAppContext())
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

    private fun getRecipes() {
        viewModelScope.launch(errorHandler) {
            val recipes = getAllRecipes()
            state.value = recipes.restoreSelections()
        }
    }
    private suspend fun getAllRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                refreshCache()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (recipesDao.getAll().isEmpty()) {
                            throw Exception("Network request failed." + "Database holds no data.")
                        }
                    }
                    else -> throw e
                }
            }
            return@withContext recipesDao.getAll()
        }
    }

    private suspend fun refreshCache() {
        val remoteRecipes = restInterface.getRecipes()
        val favoriteRecipes = recipesDao.getAllFavorited()
        recipesDao.addAll(remoteRecipes)
        recipesDao.updateAll(
            favoriteRecipes.map {
                PartialRecipe(it.id, true)
            }
        )
    }

    private suspend fun toggleFavoriteRecipe(id: Int, oldValue: Boolean) =
        withContext(Dispatchers.IO) {
            recipesDao.update(PartialRecipe(
                id = id,
                isFavorite = !oldValue)
            )
            recipesDao.getAll()
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
                .toMutableMap()
            selectedIds.forEach { id ->
                val recipe = recipesMap[id] ?: return@forEach
                recipesMap[id] = recipe.copy(isFavourite = true)
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
        viewModelScope.launch {
            val updatedRecipes = toggleFavoriteRecipe(id, recipe.isFavourite)
            state.value = updatedRecipes
        }
    }

    companion object {
        const val FAVORITES = "favorites"
    }
}
