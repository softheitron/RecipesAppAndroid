package com.example.recipesapp.di

import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}