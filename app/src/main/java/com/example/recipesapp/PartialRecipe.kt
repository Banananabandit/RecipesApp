package com.example.recipesapp

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity
class PartialRecipe (
    @ColumnInfo(name = "r_id")
    val id: Int,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)
