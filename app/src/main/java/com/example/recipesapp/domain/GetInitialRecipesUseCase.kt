package com.example.recipesapp.domain

import com.example.recipesapp.data.RecipesRepository
import javax.inject.Inject

class GetInitialRecipesUseCase @Inject constructor(
    private val repository: RecipesRepository,
    private val getSortedRecipesUseCase: GetSortedRecipesUseCase
) {
//    private val repository: RecipesRepository = RecipesRepository()
//    private val getSortedRecipesUseCase = GetSortedRecipesUseCase()
    suspend operator fun invoke(): List<Recipe> {
        repository.loadRecipes()
        return getSortedRecipesUseCase()
    }
}