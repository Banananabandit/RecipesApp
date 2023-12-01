package com.example.recipesapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity
class PartialLocalRecipe (
    @ColumnInfo(name = "r_id")
    val id: Int,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)
