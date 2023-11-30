package com.example.recipesapp

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val isFavourite: Boolean = false
)

