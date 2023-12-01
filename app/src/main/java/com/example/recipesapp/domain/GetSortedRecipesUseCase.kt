package com.example.recipesapp.domain

import com.example.recipesapp.data.RecipesRepository

class GetSortedRecipesUseCase {
    private val repository: RecipesRepository = RecipesRepository()
    suspend operator fun invoke(): List<Recipe> {
        return repository.getRecipes()
            .sortedBy { it.title }
    }
}