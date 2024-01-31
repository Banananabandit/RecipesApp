package com.example.recipesapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.data.local.RecipesDao
import com.example.recipesapp.data.local.RecipesDb
import com.example.recipesapp.data.remote.RecipesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipesModule {
    @Provides
    fun provideRoomDao(database: RecipesDb) : RecipesDao {
        return database.dao
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ): RecipesDb{
        return Room.databaseBuilder(
            appContext,
            RecipesDb::class.java,
            "recipes_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://recipesapp-7cae7-default-rtdb.firebaseio.com/")
            .build()
    }

    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): RecipesApiService {
        return retrofit.create(RecipesApiService::class.java)
    }
}