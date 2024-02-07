package com

import com.example.recipesapp.data.local.LocalRecipe
import com.example.recipesapp.data.local.PartialLocalRecipe
import com.example.recipesapp.data.local.RecipesDao
import kotlinx.coroutines.delay

class FakeRoomDao: RecipesDao {
    private var recipes = HashMap<Int, LocalRecipe>()

    override suspend fun getAll(): List<LocalRecipe> {
        delay(2000)
        return recipes.values.toList()
    }

    override suspend fun addAll(recipes: List<LocalRecipe>) {
            recipes.forEach {
                this.recipes[it.id] = it
            }
    }

    override suspend fun update(partialRecipe: PartialLocalRecipe) {
        delay(2000)
        updateRecipe(partialRecipe)
    }

    override suspend fun updateAll(partialRecipe: List<PartialLocalRecipe>) {
        delay(2000)
        partialRecipe.forEach {
            updateRecipe(it)
        }
    }

    override suspend fun getAllFavorited(): List<LocalRecipe> {
        return recipes.values.toList().filter { it.isFavourite }
    }

    private fun updateRecipe(partialRecipe: PartialLocalRecipe) {
        val recipe = this.recipes[partialRecipe.id]
        if (recipe != null)
            this.recipes[partialRecipe.id] = recipe.copy(isFavourite = partialRecipe.isFavorite)
    }
}