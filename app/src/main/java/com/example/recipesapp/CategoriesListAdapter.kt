package com.example.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.entities.Category

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val categoryImage = binding.imgCategory
        val titleText = binding.tvTitle
        val descriptionText = binding.tvDescription
        val cardItem = binding.itemCategoryCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position]
        val context = holder.itemView.context
        holder.titleText.text = category.title
        holder.descriptionText.text = category.description
        holder.cardItem.setOnClickListener { itemClickListener?.onItemClick(category.id) }

        val drawable =
            try {
                Drawable.createFromStream(context.assets.open(category.imageUrl), null)
            } catch (e: Exception) {
                Log.d("!!!", "Image file not found ${category.imageUrl}")
                null
            }
        holder.categoryImage.setImageDrawable(drawable)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int = dataSet.size

}