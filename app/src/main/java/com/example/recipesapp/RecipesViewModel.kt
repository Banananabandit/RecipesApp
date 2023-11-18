package com.example.recipesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import java.net.ConnectException
import java.net.UnknownHostException

class RecipesViewModel(): ViewModel() {

    private val restInterface: RecipesApiService
    private var recipesDao= RecipesDb.getDaoInstance(RecipesApplication.getAppContext())
    val state = mutableStateOf(emptyList<Recipe>())
    private val errorHandler = CoroutineExceptionHandler{ _, exception ->
        exception.printStackTrace()
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://recipesapp-7cae7-default-rtdb.firebaseio.com/")
            .build()

        restInterface = retrofit.create(
            RecipesApiService::class.java
        )
        getRecipes()
    }

    private fun getRecipes() {
        viewModelScope.launch(errorHandler) {
            state.value = getAllRecipes()
        }
    }
    private suspend fun getAllRecipes(): List<Recipe> {
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

    private suspend fun toggleFavoriteRecipe(id: Int, oldValue: Boolean) =
        withContext(Dispatchers.IO) {
            recipesDao.update(PartialRecipe(
                id = id,
                isFavorite = !oldValue)
            )
            recipesDao.getAll()
        }



    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRecipes = toggleFavoriteRecipe(id, oldValue)
            state.value = updatedRecipes
        }
    }
}
