package com.example.recipesapp.data

import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.data.local.LocalRecipe
import com.example.recipesapp.data.local.PartialLocalRecipe
import com.example.recipesapp.data.local.RecipesDb
import com.example.recipesapp.data.remote.RecipesApiService
import com.example.recipesapp.domain.Recipe
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

    suspend fun getRecipes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            return@withContext recipesDao.getAll().map {
                Recipe(it.id, it.title, it.description, it.isFavourite)
            }
        }
    }
    suspend fun loadRecipes() {
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
        }
    }

    private suspend fun refreshCache() {
        val remoteRecipes = restInterface.getRecipes()
        val favoriteRecipes = recipesDao.getAllFavorited()
        recipesDao.addAll(remoteRecipes.map {
            LocalRecipe(
                it.id,
                it.title,
                it.description,
                false
            )
        })
        recipesDao.updateAll(
            favoriteRecipes.map {
                PartialLocalRecipe(it.id, true)
            }
        )
    }

    suspend fun toggleFavoriteRecipe(id: Int, value: Boolean) =
        withContext(Dispatchers.IO) {
            recipesDao.update(
                PartialLocalRecipe(
                id = id,
                isFavorite = value)
            )
        }
}