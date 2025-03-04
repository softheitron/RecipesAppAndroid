package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.utils.PreferencesUtils

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesState = MutableLiveData<List<Recipe>>()
    val favoritesState: LiveData<List<Recipe>> get() = _favoritesState
    private val sharedPrefs =
        application.getSharedPreferences(RecipeFragment.FAVORITES_PREFS, Application.MODE_PRIVATE)

    fun loadRecipesByFavoritesIds() {
        val favoritesIds = PreferencesUtils.getFavorites(sharedPrefs).map { it.toInt() }.toSet()
        val recipeList = STUB.getRecipesByIds(favoritesIds)
        _favoritesState.value = recipeList
    }

}