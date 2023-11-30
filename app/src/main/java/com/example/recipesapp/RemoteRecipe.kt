package com.example.recipesapp

import com.google.gson.annotations.SerializedName

data class RemoteRecipe(
    @SerializedName("r_id")
    val id: Int,
    @SerializedName("r_name")
    val title: String,
    @SerializedName("r_description")
    val description: String,
)
