package com.example.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.example.recipesapp.ui.recipes.recipe_list.RecipesListFragment
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment(R.layout.fragment_recipe) {

    companion object {
        const val FAVORITES_PREFS = "favorites_prefs"
        const val FAVORITES_SAVE_ID = "favorites_save_id"
    }

    private var _recipeBinding: FragmentRecipeBinding? = null
    private val recipeBinding
        get() = _recipeBinding
            ?: throw IllegalStateException("Recipes List Binding, must not be null")

    private val recipeVM: RecipeViewModel by viewModels()
    private val recyclerIngredients = IngredientsAdapter()
    private val recyclerMethod = MethodAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipeBinding = FragmentRecipeBinding
            .inflate(inflater, container, false)
        val view = recipeBinding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = requireArguments()
        val recipeId = args.getInt(RecipesListFragment.ARG_RECIPE)
        initUi(recipeId)
    }

    private fun initUi(recipeId: Int) {
        initRecycler()
        applySeekBar()
        addToFavorites()
        recipeVM.loadRecipe(recipeId)
        recipeVM.recipeUiState.observe(viewLifecycleOwner) { state ->
            updateUi(state)
        }
    }

    private fun initRecycler() {
        val divider = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        ).apply {
            dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
            dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
            isLastItemDecorated = false
        }
        with(recipeBinding) {
            rvIngredients.adapter = recyclerIngredients
            rvIngredients.addItemDecoration(divider)

            rvMethod.adapter = recyclerMethod
            rvMethod.addItemDecoration(divider)
        }
    }

    private fun updateUi(state: RecipeViewModel.RecipeState) {
        val recipe = state.recipe
        with(recipeBinding) {
            if (recipe != null) {
                recyclerIngredients.dataSet = recipe.ingredients
                recyclerMethod.dataSet = recipe.method
                recyclerIngredients.updateIngredients(state.portionsAmount)
            }
            tvPortions.text =
                "${getString(R.string.recipe_portions_text)} ${state.portionsAmount}"
            imgRecipeTitle.setImageDrawable(state.recipeImage)
            tvRecipeHeader.text = state.recipe?.title
            btnAddToFavorites.setImageResource(
                if (state.iconState) R.drawable.ic_heart
                else R.drawable.ic_heart_empty
            )
        }
    }

    private fun addToFavorites() {
        recipeBinding.btnAddToFavorites.setOnClickListener {
            recipeVM.onFavoritesClicked()
        }
    }

    private fun applySeekBar() {
        recipeBinding.sbSelectPortions.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                recipeVM.updatePortionsAmount(progress)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipeBinding = null
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }
}