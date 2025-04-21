package com.example.recipesapp.data.repository

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.db.AppDatabase
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RecipesRepository(context: Context) {
    companion object {
        const val MAX_RECIPES = 99
        const val RECIPE_STANDARD_MULTIPLIER = 100
        const val CONTENT_TYPE = "application/json"
        const val BASE_API_URL = "https://recipes.androidsprint.ru/api/"
        const val IMAGES_API_URL = BASE_API_URL + "images/"
    }

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appContext = context.applicationContext

    private val db by lazy {
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "recipes-database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

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

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getRecipesFromCacheByCategory(categoryId: Int): List<Recipe> {
        return withContext(defaultDispatcher) {
            db.recipesDao().getRecipesByCategoryId(
                firstId = (categoryId * RECIPE_STANDARD_MULTIPLIER),
                lastId = (categoryId * RECIPE_STANDARD_MULTIPLIER + MAX_RECIPES)
            )
        }
    }

    suspend fun getRecipeFromCacheById(recipeId: Int): Recipe? {
        return withContext(defaultDispatcher) {
            try {
                db.recipesDao().getRecipeById(recipeId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getFavoriteRecipesFromCache(): List<Recipe> {
        return withContext(defaultDispatcher) {
            db.recipesDao().getFavoriteRecipes()
        }
    }

    suspend fun updateCachedFavoriteRecipe(recipeId: Int, isFavorite: Boolean) {
        return withContext(defaultDispatcher) {
            db.recipesDao().updateFavoritesRecipe(recipeId, isFavorite)
        }
    }

    suspend fun saveRecipesInCache(recipes: List<Recipe>) {
        return withContext(defaultDispatcher) {
            db.recipesDao().insertRecipes(recipes)
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(defaultDispatcher) {
            db.categoriesDao().getAllCategories()
        }
    }

    suspend fun saveCategoriesInCache(categories: List<Category>) {
        return withContext(defaultDispatcher) {
            db.categoriesDao().insertCategories(categories)
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(defaultDispatcher) {
            try {
                service.getCategories()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategoriesById(id: Int): List<Category>? {
        return withContext(defaultDispatcher) {
            try {
                service.getCategoriesById(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        return withContext(defaultDispatcher) {
            try {
                service.getRecipesByCategoryId(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: String): List<Recipe>? {
        return withContext(defaultDispatcher) {
            try {
                service.getRecipesByIds(ids)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        return withContext(defaultDispatcher) {
            try {
                service.getRecipeById(id)
            } catch (e: Exception) {
                null
            }
        }
    }
}