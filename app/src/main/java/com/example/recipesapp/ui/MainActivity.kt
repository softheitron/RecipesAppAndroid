package com.example.recipesapp.ui

import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

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

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val json = Json { ignoreUnknownKeys = true }
            val data = connection.inputStream.bufferedReader().readText()
            val categories = json.decodeFromString<List<Category>>(data)

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "ResponseCode: ${connection.responseCode}")
            Log.i("!!!", "ResponseMessage: ${connection.responseMessage}")
            Log.i("!!!", "ResponseBody: ${connection.inputStream.bufferedReader().readText()}")
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