package com.example.recipesapp.ui.recipes.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.ui.categories.CategoriesListFragment.Companion.ARG_CATEGORY_ID
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.utils.OnItemClickListener

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    companion object {
        const val ARG_RECIPE = "arg_recipe"
    }

    private var _recipesListBinding: FragmentListRecipesBinding? = null
    private val recipesListBinding
        get() = _recipesListBinding
            ?: throw IllegalStateException("Recipes List Binding, must not be null")
    private val recipesListVM: RecipesListViewModel by viewModels()
    private val recipesListAdapter = RecipesListAdapter()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = requireArguments()
        val categoryId = args.getInt(ARG_CATEGORY_ID)
        initUI(categoryId)
    }

    private fun initUI(categoryId: Int) {
        initRecycler()
        recipesListVM.loadRecipesByCategoryId(categoryId)
        recipesListVM.recipesListState.observe(viewLifecycleOwner) { state ->
            updateUi(state)
        }
    }

    private fun updateUi(state: RecipesListViewModel.RecipesListState) {
        recipesListAdapter.dataSet = state.recipeList
        with(recipesListBinding) {
            ivCategoryTitleImage.setImageDrawable(state.categoryImage)
            tvCategoryTitle.text = state.category?.title
        }
    }

    private fun initRecycler() {
        recipesListBinding.rvRecipes.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipeByRecipeId(itemId)
            }
        })
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE to recipeId)
        findNavController().navigate(R.id.recipeFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesListBinding = null
    }

}