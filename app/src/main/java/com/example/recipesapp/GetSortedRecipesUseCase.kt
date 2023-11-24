package com.example.recipesapp

class GetSortedRecipesUseCase {
    private val repository: RecipesRepository = RecipesRepository()
    suspend operator fun invoke(): List<Recipe> {
        return repository.getRecipes()
            .sortedBy { it.title }
    }
}