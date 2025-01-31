package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.utils.OnItemClickListener
import com.example.recipesapp.R
import com.example.recipesapp.ui.recipes.recipe_list.RecipesListFragment
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    companion object {
        const val ARG_CATEGORY_ID = "arg_category_id"
        const val ARG_CATEGORY_NAME = "arg_category_name"
        const val ARG_CATEGORY_IMAGE_URL = "arg_category_image_url"
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val recyclerAdapter = CategoriesListAdapter(STUB.getCategories())
        categoriesListBinding.rvCategories.adapter = recyclerAdapter
        recyclerAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipesByCategoryId(itemId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val categoryById = STUB.getCategories().find { id == categoryId }
        val categoryName = categoryById?.title
        val categoryImageUrl = categoryById?.imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
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