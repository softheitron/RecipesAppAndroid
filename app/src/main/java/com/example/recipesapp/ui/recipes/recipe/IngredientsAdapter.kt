package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding
import com.example.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private val dataSet: MutableList<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity = 1

    class ViewHolder(binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val description = binding.tvIngDescription
        val measure = binding.tvIngMeasure
        val quantity = binding.tvIngQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        val multipliedValue = BigDecimal(ingredient.quantity) * BigDecimal(quantity)
        val ingredientsQuantity = multipliedValue
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        holder.description.text = ingredient.description
        holder.quantity.text = ingredientsQuantity
        holder.measure.text = ingredient.unitOfMeasure
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    fun setIngredients(ingredients: List<Ingredient>) {
        dataSet.clear()
        dataSet.addAll(ingredients)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = dataSet.size

}