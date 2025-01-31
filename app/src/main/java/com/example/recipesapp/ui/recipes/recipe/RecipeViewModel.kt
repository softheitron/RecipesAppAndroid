package com.example.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    private val _recipeUiState = MutableLiveData<RecipeState>()
    val recipeUiState: LiveData<RecipeState> get() = _recipeUiState

    init {
        Log.i("!!!", "Recipe View initialized")
        _recipeUiState.value = RecipeState(iconState = true)
    }

}

data class RecipeState(
    val recipe: Recipe? = null,
    val iconState: Boolean = false,
    val portionsAmount: Int = 1,
)