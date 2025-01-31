package com.example.recipesapp

import android.content.SharedPreferences
import com.example.recipesapp.RecipeFragment.Companion.FAVORITES_SAVE_ID

object PreferencesUtils {
    fun getFavorites(sharedPrefs: SharedPreferences?): MutableSet<String> {
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_SAVE_ID, setOf()) ?: setOf())
    }
}