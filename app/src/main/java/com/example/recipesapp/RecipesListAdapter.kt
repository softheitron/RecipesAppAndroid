package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.entities.Recipe

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    class ViewHolder(binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleText = binding.tvRecipeTitle
        val recipeImage = binding.imgRecipe
        val itemCard = binding.itemRecipeCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        val context = holder.itemView.context
        holder.titleText.text = recipe.title
        holder.itemCard.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
        holder.itemCard.contentDescription = recipe.title
        val drawable =
            try {
                Drawable.createFromStream(context.assets.open(recipe.imageUrl), null)
            } catch (e: Exception) {
                Log.d("!!!", "Image file not found ${recipe.imageUrl}")
                null
            }
        holder.recipeImage.setImageDrawable(drawable)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int = dataSet.size

}