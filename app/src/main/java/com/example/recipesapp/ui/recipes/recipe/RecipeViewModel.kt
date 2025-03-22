package com.example.recipesapp.ui.recipes.recipe

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.utils.PreferencesUtils

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeUiState = MutableLiveData(RecipeState())
    val recipeUiState: LiveData<RecipeState> get() = _recipeUiState
    private var currentState = _recipeUiState.value ?: RecipeState()
    private val sharedPrefs =
        application.getSharedPreferences(RecipeFragment.FAVORITES_PREFS, Application.MODE_PRIVATE)

    private val repository = RecipesRepository()

    fun loadRecipe(recipeId: Int) {
        repository.threadPool.submit {
            val recipe = repository.getRecipeById(recipeId)
            Log.i("!!!!", recipe.toString())
            if (recipe != null) {
                val iconState = PreferencesUtils.getFavorites(sharedPrefs)
                    .contains(recipeId.toString())
                val portionsAmount = _recipeUiState.value?.portionsAmount
                val recipeImage = getImageFromAssets(recipe)
                currentState = currentState.copy(
                    recipe = recipe,
                    iconState = iconState,
                    portionsAmount = portionsAmount ?: 1,
                    recipeImage = recipeImage
                )
                _recipeUiState.postValue(currentState)
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        getApplication(),
                        "Recipe information error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getImageFromAssets(recipe: Recipe?): Drawable? {
        val categoryImage = try {
            Drawable.createFromStream(
                recipe?.let {
                    getApplication<Application>().assets.open(
                        it.imageUrl
                    )
                }, null
            )
        } catch (e: Exception) {
            Log.d(
                "!!!",
                "Image file not found ${_recipeUiState.value?.recipe?.imageUrl}"
            )
            null
        }
        return categoryImage
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

    fun updatePortionsAmount(portionsAmount: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(portionsAmount = portionsAmount)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val iconState: Boolean = false,
        val portionsAmount: Int = 1,
        val recipeImage: Drawable? = null,
    )

}
