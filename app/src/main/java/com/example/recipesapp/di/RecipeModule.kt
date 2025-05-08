package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.dao.CategoriesDao
import com.example.recipesapp.data.dao.RecipesDao
import com.example.recipesapp.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    companion object {
        const val CONTENT_TYPE = "application/json"
        const val BASE_API_URL = "https://recipes.androidsprint.ru/api/"
        const val IMAGES_API_URL = BASE_API_URL + "images/"
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @IoDispatcher
    @Provides
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "recipes-database"
        )
            .fallbackToDestructiveMigration(false)
            .build()

    @Singleton
    @Provides
    fun provideCategoriesDao(database: AppDatabase): CategoriesDao =
        database.categoriesDao()

    @Singleton
    @Provides
    fun provideRecipesDao(database: AppDatabase): RecipesDao =
        database.recipesDao()

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }

}