package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.utils.PreferencesUtils

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeUiState = MutableLiveData<RecipeState>()
    val recipeUiState: LiveData<RecipeState> get() = _recipeUiState

    private val sharedPrefs =
        application.getSharedPreferences(RecipeFragment.FAVORITES_PREFS, Application.MODE_PRIVATE)
    private val prefUtils = PreferencesUtils
    private var iconState = false

    fun loadRecipe(recipeId: Int) {
        Log.d("!!!", "Recipe loaded")
        iconState = prefUtils.getFavorites(sharedPrefs)
            .contains(recipeId.toString())
        val recipe = STUB.getRecipeById(recipeId)
        val portionsAmount = _recipeUiState.value?.portionsAmount
        _recipeUiState.value = RecipeState(
            recipe = recipe,
            iconState = iconState,
            portionsAmount = portionsAmount ?: 1
        )
    }

    fun onFavoritesClicked() {
        Log.d("!!!", "Favorites Clicked")
        val favorites = prefUtils.getFavorites(sharedPrefs)
        Log.d("!!!", "Pref Favorites: $favorites")
        iconState = !iconState
        _recipeUiState.value = _recipeUiState.value?.copy(iconState = iconState)
        val recipeId = _recipeUiState.value?.recipe?.id.toString()
        if (iconState) favorites.add(recipeId)
        else favorites.remove(recipeId)
        prefUtils.saveFavorites(sharedPrefs, favorites)
        Log.d("!!!", "Shared prefs size:${sharedPrefs.all.size}\nFavorites:$favorites")
    }

}

data class RecipeState(
    val recipe: Recipe? = null,
    val iconState: Boolean = false,
    val portionsAmount: Int = 1,
)
