package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState
    private var currentState = _categoriesState.value ?: CategoriesListState()

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesFromCache = recipesRepository.getCategoriesFromCache()
            if (categoriesFromCache.isNotEmpty()) {
                currentState = currentState.copy(
                    categoriesList = categoriesFromCache,
                    isError = false
                )
                _categoriesState.postValue(currentState)
            }
            val categoriesList = recipesRepository.getCategories()
            if (categoriesList != null && categoriesList != categoriesFromCache) {
                recipesRepository.saveCategoriesInCache(categoriesList)
                currentState = currentState.copy(
                    categoriesList = categoriesList,
                    isError = false
                )
                _categoriesState.postValue(currentState)
            } else if (categoriesFromCache.isEmpty() && categoriesList.isNullOrEmpty()) {
                currentState = currentState.copy(isError = true)
                _categoriesState.postValue(currentState)
            }
        }
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val isError: Boolean = false
    )

}