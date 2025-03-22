package com.example.recipesapp.ui.categories

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
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

    fun loadCategories(context: Context) {
        repository.threadPool.submit {
            val categoriesList = repository.getCategories()
            if (categoriesList != null) {
                currentState = currentState.copy(categoriesList = categoriesList)
                _categoriesState.postValue(currentState)
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "It seems like our server doesn't have any categories yet",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList()
    )

}