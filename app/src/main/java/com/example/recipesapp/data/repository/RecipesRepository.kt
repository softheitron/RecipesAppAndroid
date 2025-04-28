package com.example.recipesapp.data.repository

import com.example.recipesapp.data.RecipeApiService
import com.example.recipesapp.data.dao.CategoriesDao
import com.example.recipesapp.data.dao.RecipesDao
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecipesRepository(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    companion object {
        const val MAX_RECIPES = 99
        const val RECIPE_STANDARD_MULTIPLIER = 100
    }


    suspend fun getRecipesFromCacheByCategory(categoryId: Int): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getRecipesByCategoryId(
                firstId = (categoryId * RECIPE_STANDARD_MULTIPLIER),
                lastId = (categoryId * RECIPE_STANDARD_MULTIPLIER + MAX_RECIPES)
            )
        }
    }

    suspend fun getRecipeFromCacheById(recipeId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                recipesDao.getRecipeById(recipeId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getIsFavoriteFlag(recipeId: Int): Boolean? {
        return withContext(ioDispatcher) {
            recipesDao.getIsFavorite(recipeId)
        }
    }

    suspend fun updateRecipe(recipe: Recipe) {
        return withContext(ioDispatcher) {
            recipesDao.saveRecipe(recipe)
        }
    }

    suspend fun getFavoriteRecipesFromCache(): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getFavoriteRecipes()
        }
    }

    suspend fun updateCachedFavoriteRecipe(recipeId: Int, isFavorite: Boolean) {
        return withContext(ioDispatcher) {
            recipesDao.updateFavoritesRecipe(recipeId, isFavorite)
        }
    }

    suspend fun saveRecipesInCache(recipes: List<Recipe>) {
        return withContext(ioDispatcher) {
            recipesDao.insertRecipes(recipes)
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(ioDispatcher) {
            categoriesDao.getAllCategories()
        }
    }

    suspend fun saveCategoriesInCache(categories: List<Category>) {
        return withContext(ioDispatcher) {
            categoriesDao.insertCategories(categories)
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getCategories()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategoriesById(id: Int): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getCategoriesById(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                val recipesFromAPI = recipeApiService.getRecipesByCategoryId(categoryId)
                val favoriteAssociatedMap = recipesDao.getRecipesByCategoryId(
                    firstId = (categoryId * RECIPE_STANDARD_MULTIPLIER),
                    lastId = (categoryId * RECIPE_STANDARD_MULTIPLIER + MAX_RECIPES)
                ).associate {
                    it.id to it.isFavorite
                }
                val finalRecipes = recipesFromAPI.map { recipe ->
                    val isFavorite = favoriteAssociatedMap[recipe.id] ?: recipe.isFavorite
                    recipe.copy(isFavorite = isFavorite)
                }
                finalRecipes
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: String): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByIds(ids)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipeById(id)
            } catch (e: Exception) {
                null
            }
        }
    }
}