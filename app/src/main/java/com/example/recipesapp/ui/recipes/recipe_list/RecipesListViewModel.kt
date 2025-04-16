package com.example.recipesapp.ui.recipes.recipe_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState
    private var currentState = _recipesListState.value ?: RecipesListState()

    private val repository: RecipesRepository = RecipesRepository(application)

    fun loadRecipesByCategoryId(category: Category) {
        viewModelScope.launch {
            val recipeList = repository.getRecipesByCategoryId(category.id)
            if (recipeList != null) {
                currentState = currentState.copy(
                    recipeList = recipeList,
                    category = category,
                    categoryImagePath = category.imageUrl,
                    isError = false
                )
                _recipesListState.postValue(currentState)
            } else {
                currentState = currentState.copy(isError = true)
                _recipesListState.postValue(currentState)
            }
        }
    }

    data class RecipesListState(
        val recipeList: List<Recipe> = emptyList(),
        val category: Category? = null,
        val categoryImagePath: String? = null,
        val isError: Boolean = false
    )

}
