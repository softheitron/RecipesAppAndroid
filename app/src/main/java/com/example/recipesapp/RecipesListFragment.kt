package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private var _recipesListBinding : FragmentListRecipesBinding? = null
    private val recipesListBinding
        get() = _recipesListBinding
            ?: throw IllegalStateException("Recipies List Binding ,ust not me null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipesListBinding = FragmentListRecipesBinding
            .inflate(inflater, container, false)
        val view = recipesListBinding.root
        return view
    }

}