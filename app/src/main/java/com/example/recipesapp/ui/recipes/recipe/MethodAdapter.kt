package com.example.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemMethodBinding

class MethodAdapter :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    var dataSet: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val positionIncrement = 1

    class ViewHolder(binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val method = binding.tvMethod
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMethodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val method = "${position + positionIncrement}. ${dataSet[position]}"
        holder.method.text = method
    }

    override fun getItemCount(): Int = dataSet.size

}