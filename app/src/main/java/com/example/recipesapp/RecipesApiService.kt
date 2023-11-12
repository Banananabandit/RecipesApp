package com.example.recipesapp

import retrofit2.Call
import retrofit2.http.GET

interface RecipesApiService {
    @GET("recipes.json")
    fun getRecipes(): Call<List<Recipe>>
}