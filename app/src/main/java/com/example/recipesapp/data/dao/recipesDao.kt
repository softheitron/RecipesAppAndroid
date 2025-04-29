package com.example.recipesapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): Recipe?

    @Query("SELECT * FROM recipe WHERE isFavorite LIKE 1")
    suspend fun getFavoriteRecipes(): List<Recipe>

    @Query("SELECT isFavorite FROM recipe WHERE id = :recipeId")
    suspend fun getIsFavorite(recipeId: Int): Boolean?

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavoritesRecipe(recipeId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM recipe WHERE id BETWEEN :firstId AND :lastId")
    suspend fun getRecipesByCategoryId(firstId: Int, lastId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)
}