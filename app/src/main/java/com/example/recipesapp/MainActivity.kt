package com.example.recipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
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
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CategoriesListFragment>(R.id.fragmentContainerView)
            }
        }
        with(binding) {
            btnFavorites.setOnClickListener {
                supportFragmentManager.commit {
                    replace<FavoritesFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
            btnCategories.setOnClickListener {
                supportFragmentManager.commit {
                    replace<CategoriesListFragment>(R.id.fragmentContainerView)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }

}