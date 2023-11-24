package com.example.recipesapp

class GetInitialRecipesUseCase {
    private val repository: RecipesRepository = RecipesRepository()
    private val getSortedRecipesUseCase = GetSortedRecipesUseCase()
    suspend operator fun invoke(): List<Recipe> {
        repository.loadRecipes()
        return getSortedRecipesUseCase()
    }
}