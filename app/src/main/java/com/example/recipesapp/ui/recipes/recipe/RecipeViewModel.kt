package com.example.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

}

data class RecipeState(
    val recipe: Recipe? = null,
    val iconState: Boolean = false,
    val portionsAmount: Int = 1,
)