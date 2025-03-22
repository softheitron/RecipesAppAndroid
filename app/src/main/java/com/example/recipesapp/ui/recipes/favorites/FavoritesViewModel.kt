package com.example.recipesapp.ui.recipes.favorites

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.utils.PreferencesUtils

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState
    private var currentState = _favoritesState.value ?: FavoritesState()
    private val sharedPrefs =
        application.getSharedPreferences(RecipeFragment.FAVORITES_PREFS, Application.MODE_PRIVATE)

    private val repository = RecipesRepository()

    fun loadRecipesByFavoritesIds() {
        repository.threadPool.submit {
            val favoritesIds = PreferencesUtils.getFavorites(sharedPrefs).map { it.toInt() }.toSet()
            val recipeList = repository.getRecipesByIds(favoritesIds.joinToString(","))
            if (recipeList != null) {
                currentState = currentState.copy(recipeList = recipeList)
                _favoritesState.postValue(currentState)
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

    data class FavoritesState(
        val recipeList: List<Recipe> = emptyList()
    )

}