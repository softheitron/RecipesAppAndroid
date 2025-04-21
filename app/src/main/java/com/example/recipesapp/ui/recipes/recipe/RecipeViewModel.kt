package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.utils.PreferencesUtils
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeUiState = MutableLiveData(RecipeState())
    val recipeUiState: LiveData<RecipeState> get() = _recipeUiState
    private var currentState = _recipeUiState.value ?: RecipeState()
    private val sharedPrefs =
        application.getSharedPreferences(RecipeFragment.FAVORITES_PREFS, Application.MODE_PRIVATE)

    private val repository: RecipesRepository = RecipesRepository(application)

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipeFromCache = repository.getRecipeFromCacheById(recipeId)
            if (recipeFromCache != null) {
                val iconState = PreferencesUtils.getFavorites(sharedPrefs)
                    .contains(recipeId.toString())
                val portionsAmount = _recipeUiState.value?.portionsAmount
                currentState = currentState.copy(
                    recipe = recipeFromCache,
                    iconState = iconState,
                    portionsAmount = portionsAmount ?: 1,
                    recipeImagePath = recipeFromCache.imageUrl,
                    isError = false
                )
                _recipeUiState.postValue(currentState)
            }
            val recipe = repository.getRecipeById(recipeId)
            if (recipe != null) {
                val iconState = PreferencesUtils.getFavorites(sharedPrefs)
                    .contains(recipeId.toString())
                val portionsAmount = _recipeUiState.value?.portionsAmount
                currentState = currentState.copy(
                    recipe = recipe,
                    iconState = iconState,
                    portionsAmount = portionsAmount ?: 1,
                    recipeImagePath = recipe.imageUrl,
                    isError = false
                )
                _recipeUiState.postValue(currentState)
            } else if (recipeFromCache == null) {
                currentState = currentState.copy(isError = true)
                _recipeUiState.postValue(currentState)
            }
        }
    }

    fun onFavoritesClicked() {
        val favorites = PreferencesUtils.getFavorites(sharedPrefs)
        val iconState = _recipeUiState.value?.iconState
        val recipeId = _recipeUiState.value?.recipe?.id

        if (iconState != null && recipeId != null) {
            val newIconState = !iconState
            _recipeUiState.value = _recipeUiState.value?.copy(iconState = newIconState)
            if (newIconState) {
                favorites.add(recipeId.toString())
            } else {
                favorites.remove(recipeId.toString())
            }
            PreferencesUtils.saveFavorites(sharedPrefs, favorites)

            viewModelScope.launch {
                repository.updateCachedFavoriteRecipe(recipeId, newIconState)
            }
        }
    }

    fun updatePortionsAmount(portionsAmount: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(portionsAmount = portionsAmount)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val iconState: Boolean = false,
        val portionsAmount: Int = 1,
        val recipeImagePath: String? = null,
        val isError: Boolean = false
    )

}
