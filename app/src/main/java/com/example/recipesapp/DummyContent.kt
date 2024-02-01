package com.example.recipesapp

import com.example.recipesapp.domain.Recipe

object DummyContent {
    fun getDomainRecipes() = arrayListOf(
        Recipe(0, "test", "test_description")
    )
}