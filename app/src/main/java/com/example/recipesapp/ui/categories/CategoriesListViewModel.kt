package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Category

class CategoriesListViewModel : ViewModel() {

    private val _categoriesState = MutableLiveData<List<Category>>()
    val categoriesState: LiveData<List<Category>> get() = _categoriesState

    fun loadCategories() {
        val categories = STUB.getCategories()
        _categoriesState.value = categories
    }

}