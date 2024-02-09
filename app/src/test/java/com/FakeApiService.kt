package com

import com.example.recipesapp.DummyContent
import com.example.recipesapp.data.remote.RecipesApiService
import com.example.recipesapp.data.remote.RemoteRecipe
import kotlinx.coroutines.delay

class FakeApiService(error: Exception? = null) : RecipesApiService {
    private val loadingError = error
    override suspend fun getRecipes(): List<RemoteRecipe> {
        if (loadingError == null) {
            delay(2000)
            return DummyContent.getRemoteRecipes()
        }
        throw loadingError
    }

    override suspend fun getRecipe(id: Int): Map<String, RemoteRecipe> {
        TODO("Not yet implemented")
    }
}