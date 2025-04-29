package com.example.recipesapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.db.AppDatabase
import com.example.recipesapp.data.repository.RecipesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {

    companion object {
        const val CONTENT_TYPE = "application/json"
        const val BASE_API_URL = "https://recipes.androidsprint.ru/api/"
        const val IMAGES_API_URL = BASE_API_URL + "images/"
    }

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "recipes-database"
    )
        .fallbackToDestructiveMigration(false)
        .build()

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .client(client)
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val recipesRepository = RecipesRepository(
        db.recipesDao(),
        db.categoriesDao(),
        recipeApiService,
        ioDispatcher
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(recipesRepository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(recipesRepository)
    val recipeViewModelFactory = RecipeViewModelFactory(recipesRepository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(recipesRepository)

}