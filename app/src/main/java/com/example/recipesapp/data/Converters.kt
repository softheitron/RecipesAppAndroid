package com.example.recipesapp.data

import androidx.room.TypeConverter
import com.example.recipesapp.model.Ingredient
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return Json.decodeFromString(data)
    }

    @TypeConverter
    fun fromIngredientList(list: List<Ingredient>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toIngredientList(data: String): List<Ingredient> {
        return Json.decodeFromString(data)
    }
}