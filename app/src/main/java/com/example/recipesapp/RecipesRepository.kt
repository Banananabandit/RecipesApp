package com.example.recipesapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

class RecipesRepository {
    private var restInterface: RecipesApiService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://recipesapp-7cae7-default-rtdb.firebaseio.com/")
            .build()
            .create(RecipesApiService::class.java)
    private var recipesDao= RecipesDb.getDaoInstance(RecipesApplication.getAppContext())

    suspend fun getAllRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                refreshCache()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (recipesDao.getAll().isEmpty()) {
                            throw Exception("Network request failed." + "Database holds no data.")
                        }
                    }
                    else -> throw e
                }
            }
            return@withContext recipesDao.getAll()
        }
    }

    private suspend fun refreshCache() {
        val remoteRecipes = restInterface.getRecipes()
        val favoriteRecipes = recipesDao.getAllFavorited()
        recipesDao.addAll(remoteRecipes)
        recipesDao.updateAll(
            favoriteRecipes.map {
                PartialRecipe(it.id, true)
            }
        )
    }

    suspend fun toggleFavoriteRecipe(id: Int, oldValue: Boolean) =
        withContext(Dispatchers.IO) {
            recipesDao.update(PartialRecipe(
                id = id,
                isFavorite = !oldValue)
            )
            recipesDao.getAll()
        }
}