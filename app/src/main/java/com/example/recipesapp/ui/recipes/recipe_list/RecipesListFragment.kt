package com.example.recipesapp.ui.recipes.recipe_list

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
import com.example.recipesapp.ui.categories.CategoriesListFragment.Companion.ARG_CATEGORY_ID
import com.example.recipesapp.ui.categories.CategoriesListFragment.Companion.ARG_CATEGORY_IMAGE_URL
import com.example.recipesapp.ui.categories.CategoriesListFragment.Companion.ARG_CATEGORY_NAME
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    companion object {
        const val ARG_RECIPE = "arg_recipe"
    }

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    private var _recipesListBinding: FragmentListRecipesBinding? = null
    private val recipesListBinding
        get() = _recipesListBinding
            ?: throw IllegalStateException("Recipes List Binding, must not be null")

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
        categoryId = args.getInt(ARG_CATEGORY_ID)
        categoryName = args.getString(ARG_CATEGORY_NAME)
        categoryImageUrl = args.getString(ARG_CATEGORY_IMAGE_URL)
        initRecycler()
    }

    private fun initRecycler() {
        val recyclerAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        recipesListBinding.rvRecipes.adapter = recyclerAdapter
        recyclerAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipeByRecipeId(itemId)
            }
        })
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(ARG_RECIPE to recipe)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.fragmentContainerView, args = bundle)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesListBinding = null
    }

}