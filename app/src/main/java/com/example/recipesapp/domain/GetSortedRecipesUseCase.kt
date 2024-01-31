package com.example.recipesapp.domain

import com.example.recipesapp.data.RecipesRepository
import javax.inject.Inject

class GetSortedRecipesUseCase @Inject constructor(
    private val repository: RecipesRepository
) {
//    private val repository: RecipesRepository = RecipesRepository()
    suspend operator fun invoke(): List<Recipe> {
        return repository.getRecipes()
            .sortedBy { it.title }
    }
}