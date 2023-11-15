package com.example.recipesapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey()
    @ColumnInfo(name = "r_id")
    @SerializedName("r_id")
    val id: Int,
    @ColumnInfo(name = "r_name")
    @SerializedName("r_name")
    val title: String,
    @ColumnInfo(name = "r_description")
    @SerializedName("r_description")
    val description: String,
    var isFavourite: Boolean = false
)

