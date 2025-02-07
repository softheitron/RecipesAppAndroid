package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.graphics.drawable.Drawable
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

    fun loadRecipe(recipeId: Int) {
        val iconState = PreferencesUtils.getFavorites(sharedPrefs)
            .contains(recipeId.toString())
        val recipe = STUB.getRecipeById(recipeId)
        val portionsAmount = _recipeUiState.value?.portionsAmount
        val recipeImage =
            try {
                Drawable.createFromStream(
                    recipe?.let {
                        getApplication<Application>().assets.open(
                            it.imageUrl)
                    }, null)
            } catch (e: Exception) {
                Log.d(
                    "!!!",
                    "Image file not found ${_recipeUiState.value?.recipe?.imageUrl}")
                null
            }
        _recipeUiState.value = RecipeState(
            recipe = recipe,
            iconState = iconState,
            portionsAmount = portionsAmount ?: 1,
            recipeImage = recipeImage
        )
    }

    fun onFavoritesClicked() {
        val favorites = PreferencesUtils.getFavorites(sharedPrefs)
        val iconState = _recipeUiState.value?.iconState
        if (iconState != null) {
            _recipeUiState.value = _recipeUiState.value?.copy(iconState = !iconState)
            val recipeId = _recipeUiState.value?.recipe?.id.toString()
            if (!iconState) favorites.add(recipeId)
            else favorites.remove(recipeId)
            PreferencesUtils.saveFavorites(sharedPrefs, favorites)
        }
    }

}

data class RecipeState(
    val recipe: Recipe? = null,
    val iconState: Boolean = false,
    val portionsAmount: Int = 1,
    val recipeImage: Drawable? = null,
)
