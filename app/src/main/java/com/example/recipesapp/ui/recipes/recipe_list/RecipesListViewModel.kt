package com.example.recipesapp.ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState
    private var currentState = _recipesListState.value ?: RecipesListState()

    private val repository = RecipesRepository()

    fun loadRecipesByCategoryId(category: Category) {
        repository.threadPool.submit {
            val recipeList = repository.getRecipesByCategoryId(category.id)
            if (recipeList != null) {
                val categoryImage = getImageFromAssets(category)
                currentState = currentState.copy(
                    recipeList = recipeList,
                    category = category,
                    categoryImage = categoryImage
                )
                _recipesListState.postValue(currentState)
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        getApplication(),
                        "It seems like our server doesn't have any recipes yet",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getImageFromAssets(category: Category): Drawable? {
        val categoryImage = try {
            Drawable.createFromStream(
                getApplication<Application>().assets.open(
                    category.imageUrl
                ),
                null
            )
        } catch (e: Exception) {
            Log.d(
                "!!!",
                "Image file not found ${_recipesListState.value?.category?.imageUrl}"
            )
            null
        }
        return categoryImage
    }

    data class RecipesListState(
        val recipeList: List<Recipe> = emptyList(),
        val category: Category? = null,
        val categoryImage: Drawable? = null,
    )

}
