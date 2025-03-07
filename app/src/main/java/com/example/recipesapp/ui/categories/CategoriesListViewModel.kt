package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Category

class CategoriesListViewModel : ViewModel() {

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState
    private var currentState = _categoriesState.value ?: CategoriesListState()

    fun loadCategories() {
        val categoriesList = STUB.getCategories()
        currentState = currentState.copy(categoriesList = categoriesList)
        _categoriesState.value = currentState
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList()
    )

}