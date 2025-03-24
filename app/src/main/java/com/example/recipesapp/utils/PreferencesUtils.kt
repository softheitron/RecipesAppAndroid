package com.example.recipesapp.utils

import android.content.SharedPreferences
import com.example.recipesapp.ui.recipes.recipe.RecipeFragment.Companion.FAVORITES_SAVE_ID

object PreferencesUtils {
    fun getFavorites(sharedPrefs: SharedPreferences?): MutableSet<String> {
        return HashSet(sharedPrefs?.getStringSet(FAVORITES_SAVE_ID, setOf()) ?: setOf())
    }

    fun saveFavorites(sharedPrefs: SharedPreferences?, favouriteIds: Set<String>) {
        sharedPrefs?.edit()
            ?.putStringSet(FAVORITES_SAVE_ID, favouriteIds)
            ?.apply()
    }
}