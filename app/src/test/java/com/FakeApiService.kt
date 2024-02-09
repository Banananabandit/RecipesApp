package com

import com.example.recipesapp.DummyContent
import com.example.recipesapp.data.remote.RecipesApiService
import com.example.recipesapp.data.remote.RemoteRecipe
import kotlinx.coroutines.delay

class FakeApiService() : RecipesApiService {
    override suspend fun getRecipes(): List<RemoteRecipe> {
        delay(2000)
        return DummyContent.getRemoteRecipes()
    }

    override suspend fun getRecipe(id: Int): Map<String, RemoteRecipe> {
        TODO("Not yet implemented")
    }
}