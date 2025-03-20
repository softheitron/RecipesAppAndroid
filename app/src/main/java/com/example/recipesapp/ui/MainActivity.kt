package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i("!!!", "OnCreate выполняется на потоке: ${Thread.currentThread().name}")

        val threadPool = Executors.newFixedThreadPool(10)
        val json = Json { ignoreUnknownKeys = true }
        val thread = Thread {
            val categoryUrl = URL("https://recipes.androidsprint.ru/api/category")
            val categoryConnection = categoryUrl.openConnection() as HttpURLConnection
            categoryConnection.connect()

            val data = categoryConnection.inputStream.bufferedReader().readText()
            val categories = json.decodeFromString<List<Category>>(data)
            val categoriesIds = categories.map { it.id }
            categoriesIds.forEach { categoryId ->
                threadPool.submit {
                    Log.i("!!!", "Thread pool поток: ${Thread.currentThread().name}")
                    val recipesUrl =
                        URL("https://recipes.androidsprint.ru/api/category/$categoryId/recipes")
                    val recipesConnection = recipesUrl.openConnection() as HttpURLConnection
                    recipesConnection.connect()

                    val recipesData = recipesConnection.inputStream.bufferedReader().readText()
                    val recipes = json.decodeFromString<List<Recipe>>(recipesData)
                    Log.d("!!!", "Recipes: $recipes")
                }
            }

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "ResponseCode: ${categoryConnection.responseCode}")
            Log.i("!!!", "ResponseMessage: ${categoryConnection.responseMessage}")
            Log.i("!!!", "ResponseBody: $data")
            Log.i("!!!", "Категории: $categories")
        }
        thread.start()

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