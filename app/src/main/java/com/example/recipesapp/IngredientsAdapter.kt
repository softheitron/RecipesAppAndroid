package com.example.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemIngredientBinding
import com.example.recipesapp.entities.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

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
        holder.description.text = ingredient.description
        holder.quantity.text = ingredient.quantity
        holder.measure.text = ingredient.unitOfMeasure
    }

    override fun getItemCount(): Int = dataSet.size

}