package com.example.recipesapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.RecipeFragment.Companion.FAVORITES_PREFS
import com.example.recipesapp.RecipeFragment.Companion.FAVORITES_SAVE_ID
import com.example.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _favoritesFragmentBinding: FragmentFavoritesBinding? = null
    private val favoritesFragmentBinding
        get() = _favoritesFragmentBinding
            ?: throw IllegalStateException("Binding for FavoritesFragment must not be null")
    private val sharedPrefs by lazy {
        activity?.getSharedPreferences(
            FAVORITES_PREFS,
            Context.MODE_PRIVATE
        )
    }

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
        initRecycler()
    }

    private fun initRecycler() {
        val favorites = PreferencesUtils.getFavorites(sharedPrefs).map { it.toInt() }.toSet()
        if (favorites.isNotEmpty()) {
            val recyclerAdapter = FavoritesAdapter(STUB.getRecipesByIds(favorites))
            favoritesFragmentBinding.rvFavorites.adapter = recyclerAdapter
            recyclerAdapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(itemId: Int) {
                    openRecipeByRecipeId(itemId)
                }
            })
        } else {
            favoritesFragmentBinding.apply {
                rvFavorites.visibility = View.GONE
                tvFavoritesEmpty.visibility = View.VISIBLE
            }
        }
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
        _favoritesFragmentBinding = null
    }

}