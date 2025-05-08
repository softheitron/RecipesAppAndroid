package com.example.recipesapp.ui.recipes.recipe_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.di.RecipeModule.Companion.IMAGES_API_URL
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.utils.OnItemClickListener

class RecipesListAdapter :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    var dataSet: List<Recipe> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
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
        Glide
            .with(context)
            .load(IMAGES_API_URL + recipe.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.recipeImage)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int = dataSet.size

}