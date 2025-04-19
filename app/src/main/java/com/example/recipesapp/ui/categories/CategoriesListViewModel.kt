package com.example.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState
    private var currentState = _categoriesState.value ?: CategoriesListState()

    private val repository: RecipesRepository = RecipesRepository(application)

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesFromDb = repository.getCategoriesFromCache()
            if (categoriesFromDb.isNotEmpty()) {
                currentState = currentState.copy(
                    categoriesList = categoriesFromDb,
                    isError = false
                )
                _categoriesState.postValue(currentState)
            } else {
                val categoriesList = repository.getCategories()
                if (categoriesList != null) {
                    repository.saveCategoriesInCache(categoriesList)
                    currentState = currentState.copy(
                        categoriesList = categoriesList,
                        isError = false
                    )
                    _categoriesState.postValue(currentState)
                } else currentState = currentState.copy(isError = true)
            }
        }
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val isError: Boolean = false
    )

}