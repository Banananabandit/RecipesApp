package com.example.recipesapp

import android.app.Application
import android.content.Context

class RecipesApplication: Application() {
    init { app = this }
    companion object {
        private lateinit var app: RecipesApplication
        fun getAppContext(): Context = app.applicationContext
    }
}