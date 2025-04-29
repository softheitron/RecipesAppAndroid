package com.example.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipes.recipe_list.RecipesListAdapter
import com.example.recipesapp.utils.OnItemClickListener

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _favoritesFragmentBinding: FragmentFavoritesBinding? = null
    private val favoritesFragmentBinding
        get() = _favoritesFragmentBinding
            ?: throw IllegalStateException("Binding for FavoritesFragment must not be null")

    private lateinit var favoritesVM: FavoritesViewModel
    private val recyclerAdapter = RecipesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        favoritesVM = appContainer.favoritesViewModelFactory.create()
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
        initUI()
    }

    private fun initUI() {
        initRecycler()
        favoritesVM.loadRecipesByFavoritesIds()
        favoritesVM.favoritesState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
        }
    }

    private fun updateUI(state: FavoritesViewModel.FavoritesState) {
        with(favoritesFragmentBinding) {
            if (state.recipeList.isNotEmpty()) {
                recyclerAdapter.dataSet = state.recipeList
                rvFavorites.visibility = View.VISIBLE
                tvFavoritesEmpty.visibility = View.GONE
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
        val action =
            FavoritesFragmentDirections
                .actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _favoritesFragmentBinding = null
    }

}