package com.example.recipesapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Recipe::class],
    version = 2,
    exportSchema = false)
abstract class RecipesDb : RoomDatabase() {
    abstract val dao: RecipesDao
    companion object {
        @Volatile
        private var INSTANCE: RecipesDao? = null

        fun getDaoInstance(context: Context): RecipesDao {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = buildDatabase(context).dao
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun buildDatabase(context: Context): RecipesDb =
                Room.databaseBuilder(
                    context.applicationContext,
                    RecipesDb::class.java,
                    "recipes_database")
                    .fallbackToDestructiveMigration()
                    .build()
    }
}