package com.example.recipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnFavorites.setOnClickListener {
                findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.favoritesFragment)
            }
            btnCategories.setOnClickListener {
                findNavController(R.id.fragmentContainerView)
                    .navigate(R.id.categoriesListFragment)
            }
        }
    }

}