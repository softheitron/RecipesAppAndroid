package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private var _categoriesListBinding: FragmentListCategoriesBinding? = null
    private val categoriesListBinding
        get() = _categoriesListBinding
            ?: throw IllegalStateException("Binding for CategoriesListFragment must not be null")

    private val categoriesVM: CategoriesListViewModel by viewModels()
    private val categoriesListAdapter = CategoriesListAdapter()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initRecycler()
        categoriesVM.loadCategories()
        categoriesVM.categoriesState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
        }
    }

    private fun updateUI(state: CategoriesListViewModel.CategoriesListState) {
        if (state.isError) {
            Toast.makeText(
                requireContext(),
                "Couldn't find any categories yet",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            categoriesListAdapter.dataSet = state.categoriesList
        }
    }

    private fun initRecycler() {
        categoriesListBinding.rvCategories.adapter = categoriesListAdapter
        categoriesListAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipesByCategoryId(itemId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category =
            categoriesVM.categoriesState.value?.categoriesList?.find { it.id == categoryId }
        category?.let {
            val action = CategoriesListFragmentDirections
                .actionCategoriesListFragmentToRecipesListFragment(it)
            findNavController().navigate(action)
        } ?: throw IllegalArgumentException("Category must not be null!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _categoriesListBinding = null
    }

}