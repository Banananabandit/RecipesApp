package com.example.recipesapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApiService {
    @GET("recipes.json")
    suspend fun getRecipes(): List<RemoteRecipe>

    @GET("recipes.json?orderBy=\"r_id\"")
    suspend fun getRecipe(
        @Query("equalTo")
        id: Int
    ): Map<String, RemoteRecipe>

}