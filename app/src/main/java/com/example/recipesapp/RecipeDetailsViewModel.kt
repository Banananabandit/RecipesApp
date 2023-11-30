package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeDetailsViewModel(private val stateHandle: SavedStateHandle): ViewModel() {
    private var restInterface: RecipesApiService
    val state = mutableStateOf<Recipe?>(null)

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://recipesapp-7cae7-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(RecipesApiService::class.java)
        val id = stateHandle.get<Int>("recipe_id") ?: 0
        viewModelScope.launch {
            val recipe = getRemoteRecipe(id)
            state.value = recipe
        }
    }

    private suspend fun getRemoteRecipe(id: Int): Recipe {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRecipe(id)
            return@withContext responseMap.values.first().let {
                Recipe(
                    id = it.id,
                    title = it.title,
                    description = it.description
                )
            }
        }
    }
}