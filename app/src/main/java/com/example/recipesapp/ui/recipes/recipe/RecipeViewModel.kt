package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _recipeUiState = MutableLiveData(RecipeState())
    val recipeUiState: LiveData<RecipeState> get() = _recipeUiState
    private var currentState = _recipeUiState.value ?: RecipeState()

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val portionsAmount = _recipeUiState.value?.portionsAmount
            val recipeFromCache = recipesRepository.getRecipeFromCacheById(recipeId)
            if (recipeFromCache != null) {
                currentState = currentState.copy(
                    recipe = recipeFromCache,
                    portionsAmount = portionsAmount ?: 1,
                    recipeImagePath = recipeFromCache.imageUrl,
                    isError = false
                )
                _recipeUiState.postValue(currentState)
            }

            val recipe = recipesRepository.getRecipeById(recipeId)
            if (recipe != null && recipe != recipeFromCache) {
                val isFavorite = recipesRepository.getIsFavoriteFlag(recipeId) ?: false
                val finalRecipe = recipe.copy(isFavorite = isFavorite)
                currentState = currentState.copy(
                    recipe = finalRecipe,
                    portionsAmount = portionsAmount ?: 1,
                    recipeImagePath = recipe.imageUrl,
                    isError = false
                )
                _recipeUiState.postValue(currentState)
                recipesRepository.updateRecipe(finalRecipe)
            } else if (recipe == null && recipeFromCache == null) {
                currentState = currentState.copy(isError = true)
                _recipeUiState.postValue(currentState)
            }
        }
    }

fun onFavoritesClicked() {
    var recipe = _recipeUiState.value?.recipe
    if (recipe != null) {
        val isFavorite = recipe.isFavorite
        recipe = recipe.copy(isFavorite = !isFavorite)
        _recipeUiState.value = _recipeUiState.value?.copy(recipe = recipe)

        viewModelScope.launch {
            recipesRepository.updateRecipe(recipe)
        }
    }
}

fun updatePortionsAmount(portionsAmount: Int) {
    _recipeUiState.value = _recipeUiState.value?.copy(portionsAmount = portionsAmount)
}

data class RecipeState(
    val recipe: Recipe? = null,
    val portionsAmount: Int = 1,
    val recipeImagePath: String? = null,
    val isError: Boolean = false
)

}
