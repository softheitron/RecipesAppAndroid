package com.example.recipesapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<Recipe>)
}