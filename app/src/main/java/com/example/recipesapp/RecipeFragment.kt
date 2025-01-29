package com.example.recipesapp

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentRecipeBinding
import com.example.recipesapp.entities.Recipe
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
    private var iconState = false
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
        val favourites = PreferencesUtils.getFavorites(sharedPrefs)
        iconState = favourites.contains(recipe.id.toString())
        setButtonByIconState()
        with(recipeBinding) {
            imgRecipeTitle.setImageDrawable(drawable)
            tvRecipeHeader.text = recipe.title
            tvPortions.text =
                "${getString(R.string.recipe_portions_text)} ${sbSelectPortions.progress}"
            btnAddToFavorites.setOnClickListener {
                iconState = !iconState
                setButtonByIconState()
                changeFavoritesByIconState(recipe, favourites)
            }
        }
    }

    private fun setButtonByIconState() {
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

    private fun changeFavoritesByIconState(recipe: Recipe, favorites: MutableSet<String>) {
        if (iconState) favorites.add(recipe.id.toString())
        else favorites.remove(recipe.id.toString())
        saveFavorites(favorites)
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
                    tvPortions.text = "${getString(R.string.recipe_portions_text)} $progress"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }
    }

    private fun saveFavorites(favouriteIds: Set<String>) {
        sharedPrefs?.edit()
            ?.putStringSet(FAVORITES_SAVE_ID, favouriteIds)
            ?.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipeBinding = null
    }

}