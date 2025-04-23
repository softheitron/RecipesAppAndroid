package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState
    private var currentState = _favoritesState.value ?: FavoritesState()

    private val repository: RecipesRepository = RecipesRepository(application)

    fun loadRecipesByFavoritesIds() {
        viewModelScope.launch {
            val favoriteRecipesFromCache = repository.getFavoriteRecipesFromCache()
            if (favoriteRecipesFromCache.isNotEmpty()) {
                currentState = currentState.copy(recipeList = favoriteRecipesFromCache)
                _favoritesState.postValue(currentState)
            }
        }
    }

    data class FavoritesState(
        val recipeList: List<Recipe> = emptyList()
    )

}