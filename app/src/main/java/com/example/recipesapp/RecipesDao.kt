package com.example.recipesapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(recipes: List<Recipe>)

    @Update(entity = Recipe::class)
    suspend fun update(partialRecipe: PartialRecipe)
}