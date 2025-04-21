package com.example.recipesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipesapp.data.Converters
import com.example.recipesapp.data.dao.CategoriesDao
import com.example.recipesapp.data.dao.RecipesDao
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}