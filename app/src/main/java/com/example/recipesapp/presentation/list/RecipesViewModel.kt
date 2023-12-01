package com.example.recipesapp.presentation.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.domain.GetInitialRecipesUseCase
import com.example.recipesapp.domain.ToggleFavoriteRecipeUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RecipesViewModel: ViewModel() {
    private val getInitialRecipeUseCase = GetInitialRecipesUseCase()
    private val toggleFavoriteRecipeUseCase = ToggleFavoriteRecipeUseCase()
    private val _state = mutableStateOf(
        RecipeScreenState(
            recipes = listOf(),
            isLoading = true
        )
    )

    val state: State<RecipeScreenState>
        get() = _state
    private val errorHandler = CoroutineExceptionHandler{ _, exception ->
        exception.printStackTrace()
        _state.value = _state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    init {
        getRecipes()
    }

    private fun getRecipes() {
        viewModelScope.launch(errorHandler) {
            val recipes = getInitialRecipeUseCase()
            _state.value = _state.value.copy(
                recipes = recipes,
                isLoading = false
            )
        }
    }
    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRecipes = toggleFavoriteRecipeUseCase(id, oldValue)
            _state.value = _state.value.copy(
                recipes = updatedRecipes
            )
        }
    }
}
