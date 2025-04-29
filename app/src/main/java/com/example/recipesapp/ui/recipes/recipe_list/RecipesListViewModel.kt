package com.example.recipesapp.ui.recipes.recipe_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState
    private var currentState = _recipesListState.value ?: RecipesListState()

    fun loadRecipesByCategoryId(category: Category) {
        _recipesListState.postValue(
            currentState.copy(
                category = category,
                categoryImagePath = category.imageUrl
            )
        )
        viewModelScope.launch {
            val recipesFromCache = recipesRepository.getRecipesFromCacheByCategory(category.id)
            if (recipesFromCache.isNotEmpty()) {
                currentState = currentState.copy(
                    recipeList = recipesFromCache,
                    category = category,
                    categoryImagePath = category.imageUrl,
                    isError = false
                )
                _recipesListState.postValue(currentState)
            }
            val recipeList = recipesRepository.getRecipesByCategoryId(category.id)
            if (recipeList != null && recipeList != recipesFromCache) {
                currentState = currentState.copy(
                    recipeList = recipeList,
                    category = category,
                    categoryImagePath = category.imageUrl,
                    isError = false
                )
                _recipesListState.postValue(currentState)
                recipesRepository.saveRecipesInCache(recipeList)
            } else if (recipesFromCache.isEmpty() && recipeList.isNullOrEmpty()) {
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
