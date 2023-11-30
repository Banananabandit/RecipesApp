package com.example.recipesapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<LocalRecipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(recipes: List<LocalRecipe>)

    @Update(entity = LocalRecipe::class)
    suspend fun update(partialRecipe: PartialLocalRecipe)

    @Update(entity = LocalRecipe::class)
    suspend fun updateAll(partialRecipe: List<PartialLocalRecipe>)

    @Query("SELECT * FROM recipes WHERE is_favorite = 1")
    suspend fun getAllFavorited(): List<LocalRecipe>
}