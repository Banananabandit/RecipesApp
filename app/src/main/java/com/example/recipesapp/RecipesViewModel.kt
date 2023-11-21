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

class RecipesViewModel: ViewModel() {
    private val repository = RecipesRepository()
    val state = mutableStateOf(
        RecipeScreenState(
            recipes = listOf(),
            isLoading = true
        )
    )
    private val errorHandler = CoroutineExceptionHandler{ _, exception ->
        exception.printStackTrace()
        state.value = state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    init {
        getRecipes()
    }

    private fun getRecipes() {
        viewModelScope.launch(errorHandler) {
            val recipes = repository.getAllRecipes()
            state.value = state.value.copy(
                recipes = recipes,
                isLoading = false
            )
        }
    }
    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRecipes = repository.toggleFavoriteRecipe(id, oldValue)
            state.value = state.value.copy(
                recipes = updatedRecipes
            )
        }
    }
}
