package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category

class CategoriesListViewModel : ViewModel() {

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState
    private var currentState = _categoriesState.value ?: CategoriesListState()

    private val repository = RecipesRepository()

    fun loadCategories() {
        repository.threadPool.submit {
            val categoriesList = repository.getCategories()
            if (categoriesList != null) {
                currentState = currentState.copy(
                    categoriesList = categoriesList,
                    isError = false)
                _categoriesState.postValue(currentState)
            } else {
                currentState = currentState.copy(isError = true)
            }
        }
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val isError: Boolean = false
    )

}