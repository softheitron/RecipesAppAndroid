package com.example.recipesapp.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
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
        initRecycler(recipeId)
    }

    private fun initUi(recipeId: Int) {
        recipeVM.loadRecipe(recipeId)
        recipeVM.recipeUiState.observe(viewLifecycleOwner) { state ->
            val drawable =
                try {
                    Drawable.createFromStream(state.recipe?.let { recipe ->
                        requireContext().assets.open(
                            recipe.imageUrl)
                    }, null)
                } catch (e: Exception) {
                    Log.d("!!!", "Image file not found ${state.recipe?.imageUrl}")
                    null
                }
            setButtonByIconState(state.iconState)
            with(recipeBinding) {
                imgRecipeTitle.setImageDrawable(drawable)
                tvRecipeHeader.text = state.recipe?.title
                tvPortions.text =
                    "${getString(R.string.recipe_portions_text)} ${sbSelectPortions.progress}"
                btnAddToFavorites.setOnClickListener {
                    Log.d("!!!", "Favorites BUTTON Clicked")
                    setButtonByIconState(state.iconState)
                    recipeVM.onFavoritesClicked()
                }
            }
        }

    }

    private fun setButtonByIconState(iconState: Boolean) {
        with(recipeBinding) {
            btnAddToFavorites.apply {
                if (iconState) {
                    setImageResource(R.drawable.ic_heart)
                } else {
                    setImageResource(R.drawable.ic_heart_empty)
                }
            }
        }
    }

    private fun initRecycler(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        if (recipe != null) {
            val recyclerIngredients = IngredientsAdapter(recipe.ingredients)
            val recyclerMethod = MethodAdapter(recipe.method)
            val divider = MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            ).apply {
                dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
                dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
                isLastItemDecorated = false
            }
            with(recipeBinding) {
                rvIngredients.apply {
                    adapter = recyclerIngredients
                    addItemDecoration(divider)
                }
                rvMethod.apply {
                    adapter = recyclerMethod
                    addItemDecoration(divider)
                }
                sbSelectPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        recyclerIngredients.updateIngredients(progress)
                        tvPortions.text = "${getString(R.string.recipe_portions_text)} $progress"
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipeBinding = null
    }

}