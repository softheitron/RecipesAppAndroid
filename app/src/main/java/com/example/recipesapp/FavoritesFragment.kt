package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _favoritesFragmentBinding: FragmentFavoritesBinding? = null
    private val favoritesFragmentBinding
        get() = _favoritesFragmentBinding
            ?: throw IllegalStateException("Binding for FavoritesFragment must not be null")

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

}