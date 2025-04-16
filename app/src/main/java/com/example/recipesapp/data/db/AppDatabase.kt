package com.example.recipesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipesapp.data.dao.CategoriesDao
import com.example.recipesapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}