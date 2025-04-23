package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeUiState = MutableLiveData(RecipeState())
    val recipeUiState: LiveData<RecipeState> get() = _recipeUiState
    private var currentState = _recipeUiState.value ?: RecipeState()

    private val repository: RecipesRepository = RecipesRepository(application)

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipeFromCache = repository.getRecipeFromCacheById(recipeId)
            if (recipeFromCache != null) {
                val portionsAmount = _recipeUiState.value?.portionsAmount
                currentState = currentState.copy(
                    recipe = recipeFromCache,
                    iconState = recipeFromCache.isFavorite,
                    portionsAmount = portionsAmount ?: 1,
                    recipeImagePath = recipeFromCache.imageUrl,
                    isError = false
                )
                _recipeUiState.postValue(currentState)
            }
            val recipe = repository.getRecipeById(recipeId)
            if (recipe != null) {
                val portionsAmount = _recipeUiState.value?.portionsAmount
                currentState = currentState.copy(
                    recipe = recipe,
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
        val iconState = _recipeUiState.value?.iconState
        val recipeId = _recipeUiState.value?.recipe?.id

        if (iconState != null && recipeId != null) {
            val newIconState = !iconState
            _recipeUiState.value = _recipeUiState.value?.copy(iconState = newIconState)

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
