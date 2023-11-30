package com.example.recipesapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recipes")
data class LocalRecipe(
    @PrimaryKey()
    @ColumnInfo(name = "r_id")
    val id: Int,
    @ColumnInfo(name = "r_name")
    val title: String,
    @ColumnInfo(name = "r_description")
    val description: String,
    @ColumnInfo(name = "is_favorite")
    val isFavourite: Boolean = false
)
