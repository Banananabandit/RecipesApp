package com.example.recipesapp

import com.example.recipesapp.data.remote.RemoteRecipe
import com.example.recipesapp.domain.Recipe

object DummyContent {
    fun getDomainRecipes() = arrayListOf(
        Recipe(0, "test", "test_description"),
        Recipe(1, "test1", "test_description1"),
        Recipe(2, "test2", "test_description2"),
        Recipe(3, "test3", "test_description3"),
        Recipe(4, "test4", "test_description4")
    )

    fun getRemoteRecipes() = getDomainRecipes()
        .map { recipe ->
            RemoteRecipe(
                recipe.id,
                recipe.title,
                recipe.description
            )
        }
}