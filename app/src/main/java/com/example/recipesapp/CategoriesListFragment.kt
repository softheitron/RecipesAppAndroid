package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private var _categoriesListBinding: FragmentListCategoriesBinding? = null
    private val categoriesListBinding
        get() = _categoriesListBinding
            ?: throw IllegalStateException("Binding for CategoriesListFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _categoriesListBinding = FragmentListCategoriesBinding
            .inflate(inflater, container, false)
        val view = categoriesListBinding.root
        return view

    }

}