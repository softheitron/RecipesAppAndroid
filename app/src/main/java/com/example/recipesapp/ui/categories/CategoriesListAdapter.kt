package com.example.recipesapp.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.utils.OnItemClickListener

class CategoriesListAdapter : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    var dataSet: List<Category> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var itemClickListener: OnItemClickListener? = null

    class ViewHolder(binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val categoryImage = binding.imgCategory
        val titleText = binding.tvCategoryTitle
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
        holder.cardItem.contentDescription = category.title
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