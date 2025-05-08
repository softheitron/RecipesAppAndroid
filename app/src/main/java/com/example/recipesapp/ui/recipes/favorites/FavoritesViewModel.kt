package com.example.recipesapp.ui.recipes.favorites

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
class FavoritesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState
    private var currentState = _favoritesState.value ?: FavoritesState()

    fun loadRecipesByFavoritesIds() {
        viewModelScope.launch {
            val favoriteRecipesFromCache = recipesRepository.getFavoriteRecipesFromCache()
            if (favoriteRecipesFromCache.isNotEmpty()) {
                currentState = currentState.copy(recipeList = favoriteRecipesFromCache)
                _favoritesState.postValue(currentState)
            }
        }
    }

    data class FavoritesState(
        val recipeList: List<Recipe> = emptyList()
    )

}