package com.example.recipesapp.ui.recipes.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.data.di.AppContainer.Companion.IMAGES_API_URL
import com.example.recipesapp.databinding.FragmentListRecipesBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.utils.OnItemClickListener

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private var _recipesListBinding: FragmentListRecipesBinding? = null
    private val recipesListBinding
        get() = _recipesListBinding
            ?: throw IllegalStateException("Recipes List Binding, must not be null")
    private lateinit var recipesListVM: RecipesListViewModel
    private val recipesListAdapter = RecipesListAdapter()
    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        recipesListVM = appContainer.recipesListViewModelFactory.create()
    }

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
        val category = args.category
        initUI(category)
    }

    private fun initUI(category: Category) {
        initRecycler()
        recipesListVM.loadRecipesByCategoryId(category)
        recipesListVM.recipesListState.observe(viewLifecycleOwner) { state ->
            updateUi(state)
        }
    }

    private fun updateUi(state: RecipesListViewModel.RecipesListState) {
        if (state.isError) {
            Toast.makeText(
                requireContext(),
                "Couldn't find recipe list",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            recipesListAdapter.dataSet = state.recipeList
            with(recipesListBinding) {
                Glide
                    .with(requireContext())
                    .load(IMAGES_API_URL + state.categoryImagePath)
                    .centerCrop()
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(ivCategoryTitleImage)
                tvCategoryTitle.text = state.category?.title
            }
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
        val action =
            RecipesListFragmentDirections
                .actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesListBinding = null
    }

}