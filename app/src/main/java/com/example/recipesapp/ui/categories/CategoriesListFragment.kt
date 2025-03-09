package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.ui.recipes.recipe_list.RecipesListFragment
import com.example.recipesapp.utils.OnItemClickListener

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    companion object {
        const val ARG_CATEGORY_ID = "arg_category_id"
    }

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
        categoriesListAdapter.dataSet = state.categoriesList
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
        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
        )
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.fragmentContainerView, args = bundle)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _categoriesListBinding = null
    }

}