package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

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
    }

}