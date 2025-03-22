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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
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

        threadPool.submit {
            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            val categoryUrl = "https://recipes.androidsprint.ru/api/category"
            val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            client.newCall(createRequestByUrl(categoryUrl)).execute().use { categoryResponse ->
                val data = categoryResponse.body?.string() ?: ""
                val categories = json.decodeFromString<List<Category>>(data)
                val categoriesIds = categories.map { it.id }
                categoriesIds.forEach { categoryId ->
                    Log.i("!!!", "Thread pool поток: ${Thread.currentThread().name}")
                    val recipesUrl = "$categoryUrl/$categoryId/recipes"
                    client.newCall(createRequestByUrl(recipesUrl)).execute()
                        .use { recipeResponse ->
                            val recipesData = recipeResponse.body?.string() ?: ""
                            val recipes = json.decodeFromString<List<Recipe>>(recipesData)
                            Log.d("!!!", "Recipes: $recipes")
                        }
                }
            }
        }

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

private fun createRequestByUrl(url: String): Request {
    val request = Request.Builder()
        .url(url)
        .build()
    return request
}