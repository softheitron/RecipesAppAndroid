package com.example.recipesapp.ui.recipes.favorites

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
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment
import com.example.recipesapp.ui.recipes.recipe_list.RecipesListAdapter
import com.example.recipesapp.ui.recipes.recipe_list.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.utils.OnItemClickListener

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _favoritesFragmentBinding: FragmentFavoritesBinding? = null
    private val favoritesFragmentBinding
        get() = _favoritesFragmentBinding
            ?: throw IllegalStateException("Binding for FavoritesFragment must not be null")

    private val favoritesVM: FavoritesViewModel by viewModels()
    private val recyclerAdapter = RecipesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _favoritesFragmentBinding = FragmentFavoritesBinding
            .inflate(inflater, container, false)
        val view = favoritesFragmentBinding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initRecycler()
        favoritesVM.loadRecipesByFavoritesIds()
        favoritesVM.favoritesState.observe(viewLifecycleOwner) { recipes ->
            updateUI(recipes)
        }
    }

    private fun updateUI(recipes: List<Recipe>) {
        with(favoritesFragmentBinding) {
            if (recipes.isNotEmpty()) {
                recyclerAdapter.dataSet = recipes
            } else {
                rvFavorites.visibility = View.GONE
                tvFavoritesEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecycler() {
        favoritesFragmentBinding.rvFavorites.adapter = recyclerAdapter
        recyclerAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipeByRecipeId(itemId)
            }
        })
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE to recipeId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.fragmentContainerView, args = bundle)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _favoritesFragmentBinding = null
    }

}