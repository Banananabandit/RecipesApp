package com.example.recipesapp.domain

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val isFavourite: Boolean = false
)

