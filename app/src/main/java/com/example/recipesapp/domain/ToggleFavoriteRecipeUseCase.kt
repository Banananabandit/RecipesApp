package com.example.recipesapp.domain

import com.example.recipesapp.data.RecipesRepository
import javax.inject.Inject

class ToggleFavoriteRecipeUseCase @Inject constructor(
    private val repository: RecipesRepository,
    private val getSortedRecipesUseCase: GetSortedRecipesUseCase
) {
//    private val repository: RecipesRepository = RecipesRepository()
//    private val getSortedRecipesUseCase = GetSortedRecipesUseCase()
    suspend operator fun invoke(id: Int, oldValue: Boolean): List<Recipe> {
        val newFavorite = oldValue.not()
        repository.toggleFavoriteRecipe(id, newFavorite)
        return getSortedRecipesUseCase()
    }
}