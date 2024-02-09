package com.example.recipesapp.data

import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.data.di.IODispatcher
import com.example.recipesapp.data.local.LocalRecipe
import com.example.recipesapp.data.local.PartialLocalRecipe
import com.example.recipesapp.data.local.RecipesDao
import com.example.recipesapp.data.local.RecipesDb
import com.example.recipesapp.data.remote.RecipesApiService
import com.example.recipesapp.domain.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepository @Inject constructor(
    private val restInterface: RecipesApiService,
    private val recipesDao: RecipesDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun getRecipes(): List<Recipe> {
        return withContext(dispatcher) {
            return@withContext recipesDao.getAll().map {
                Recipe(it.id, it.title, it.description, it.isFavourite)
            }
        }
    }
    suspend fun loadRecipes() {
        return withContext(dispatcher) {
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
        withContext(dispatcher) {
            recipesDao.update(
                PartialLocalRecipe(
                id = id,
                isFavorite = value)
            )
        }
}