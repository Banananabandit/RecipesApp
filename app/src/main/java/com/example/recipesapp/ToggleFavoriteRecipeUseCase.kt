package com.example.recipesapp

class ToggleFavoriteRecipeUseCase {
    private val repository: RecipesRepository = RecipesRepository()
    private val getSortedRecipesUseCase = GetSortedRecipesUseCase()
    suspend operator fun invoke(id: Int, oldValue: Boolean): List<Recipe> {
        val newFavorite = oldValue.not()
        repository.toggleFavoriteRecipe(id, newFavorite)
        return getSortedRecipesUseCase()
    }
}