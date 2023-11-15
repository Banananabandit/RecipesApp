package com.example.recipesapp

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApiService {
    @GET("recipes.json")
    suspend fun getRecipes(): List<Recipe>

    @GET("recipes.json?orderBy=\"r_id\"")
    suspend fun getRecipe(
        @Query("equalTo")
        id: Int
    ): Map<String, Recipe>

}