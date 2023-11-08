package com.example.recipesapp

data class Recipe(
    val id: Int,
    val title: String,
    val description: String
)

val dummyListRecipes = listOf<Recipe>(
    Recipe(0, "Chicken Teriyaki", "Japanese classic quick recipe"),
    Recipe(1, "Spaghetti Puttanesca", "Italian quick pantry ingredient recipe"),
    Recipe(2, "Sushi rice", "Fool proof sushi recipe"),
    Recipe(3, "Beef mince stroganoff", "Intermediate skill hungarian pasta dish"),
    Recipe(4, "Takoyaki", "Japanese savoury crepe balls"),
    Recipe(5, "Kung Pao chicken", "Chinese takeaway classic"),
    Recipe(6, "Chicken with basil", "Thai recipe that uses fish sauce"),
    Recipe(7, "Sweet and Sour Chicken", "Another classic chinese takeaway recipe"),
    Recipe(8, "Shredded chicken with pickled chilies", "Hot, sour and sweet Sichuan recipe"),
    Recipe(9, "Chungking Pork", "Simple chinese recipe with cabbage"),
    Recipe(10, "Pork and cucumber", "Chinese recipe that uses fresh cucumbers"),
    Recipe(12, "Pad Ka-Prao", "Classic Thai recipe with ground beef and basil"),
    Recipe(13, "Beef with broccoli", "Chinese recipe that uses black bean sauce"),
    Recipe(14, "Khai Jiao", "A thai style omelet"),
)
