package com.example.recipesapp.domain

import com.example.recipesapp.data.RecipesRepository

class GetInitialRecipesUseCase {
    private val repository: RecipesRepository = RecipesRepository()
    private val getSortedRecipesUseCase = GetSortedRecipesUseCase()
    suspend operator fun invoke(): List<Recipe> {
        repository.loadRecipes()
        return getSortedRecipesUseCase()
    }
}