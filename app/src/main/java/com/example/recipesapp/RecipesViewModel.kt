package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class RecipesViewModel(private val stateHandle: SavedStateHandle): ViewModel() {

    private var restInterface: RecipesApiService

    val state = mutableStateOf(emptyList<Recipe>())

    private lateinit var recipesCall: Call<List<Recipe>>

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
        recipesCall = restInterface.getRecipes()
        recipesCall.enqueue(
            object : Callback<List<Recipe>> {
                override fun onResponse(
                    call: Call<List<Recipe>>,
                    response: Response<List<Recipe>>
                ) {
                    response.body()?.let { recipes ->
                        state.value = recipes.restoreSelections()
                    }
                }

                override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                    t.printStackTrace()
                }

            }
        )

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

    override fun onCleared() {
        super.onCleared()
        recipesCall.cancel()
    }

    companion object {
        const val FAVORITES = "favorites"
    }
}
