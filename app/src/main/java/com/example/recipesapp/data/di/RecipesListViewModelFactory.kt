package com.example.recipesapp.data.di

import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.ui.recipes.recipe_list.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}