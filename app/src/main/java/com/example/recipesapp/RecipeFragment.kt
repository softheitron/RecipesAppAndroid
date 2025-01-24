package com.example.recipesapp

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.example.recipesapp.entities.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment(R.layout.fragment_recipe) {

    private var _recipeBinding: FragmentRecipeBinding? = null
    private val recipeBinding
        get() = _recipeBinding
            ?: throw IllegalStateException("Recipes List Binding, must not be null")

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
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getParcelable(RecipesListFragment.ARG_RECIPE, Recipe::class.java)
        } else {
            args.getParcelable(RecipesListFragment.ARG_RECIPE)
        }

        recipe?.let {
            initUi(it)
            initRecycler(it)
        }
    }

    private fun initUi(recipe: Recipe) {
        val drawable =
            try {
                Drawable.createFromStream(requireContext().assets.open(recipe.imageUrl), null)
            } catch (e: Exception) {
                Log.d("!!!", "Image file not found ${recipe.imageUrl}")
                null
            }
        with(recipeBinding) {
            imgRecipeTitle.setImageDrawable(drawable)
            tvRecipeHeader.text = recipe.title
            tvPortionsAmount.text = String.format(sbSelectPortions.progress.toString())
        }
    }

    private fun initRecycler(recipe: Recipe) {
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
                    tvPortionsAmount.text = String.format(progress.toString())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?){}

                override fun onStopTrackingTouch(seekBar: SeekBar?){}

            })
        }
    }

}