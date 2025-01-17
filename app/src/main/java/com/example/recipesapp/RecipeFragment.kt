package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment(R.layout.fragment_recipe) {

    private var _recipeBinding: FragmentRecipeBinding? = null
    private val recipeBinding
        get() = _recipeBinding
            ?: throw IllegalStateException("Recipes List Binding, must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipeBinding = FragmentRecipeBinding
            .inflate(inflater, container, false)
        val view = recipeBinding.root
        return view
    }

}