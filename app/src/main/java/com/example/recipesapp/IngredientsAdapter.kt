package com.example.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding
import com.example.recipesapp.entities.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
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
        val multipliedValue = ingredient.quantity.toFloat().times(quantity)
        val ingredientsQuantity =
            if (multipliedValue % 1.0 == 0.0) {
                multipliedValue.toInt().toString()
            } else {
                "%.1f".format(multipliedValue)
            }
        holder.description.text = ingredient.description
        holder.quantity.text = ingredientsQuantity
        holder.measure.text = ingredient.unitOfMeasure
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataSet.size

}