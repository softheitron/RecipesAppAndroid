package com.example.recipesapp.data.repository

import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.Executors

class RecipesRepository {
    companion object {
        const val CONTENT_TYPE = "application/json"
        const val BASE_API_URL = "https://recipes.androidsprint.ru/api/"
        const val IMAGES_API_URL = BASE_API_URL + "images/"
    }

    val threadPool = Executors.newFixedThreadPool(10)

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

    fun getCategories(): List<Category>? {
        return try {
            service.getCategories()
                .execute()
                .body()
        } catch (e: Exception) {
            null
        }
    }

    fun getCategoriesById(id: Int): List<Category>? {
        return try {
            service.getCategoriesById(id)
                .execute()
                .body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        return try {
            service.getRecipesByCategoryId(id)
                .execute()
                .body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByIds(ids: String): List<Recipe>? {
        return try {
            service.getRecipesByIds(ids)
                .execute()
                .body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipeById(id: Int): Recipe? {
        return try {
            service.getRecipeById(id)
                .execute()
                .body()
        } catch (e: Exception) {
            null
        }
    }
}