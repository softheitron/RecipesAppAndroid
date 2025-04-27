package com.example.recipesapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.di.AppContainer.Companion.IMAGES_API_URL
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
        Glide
            .with(context)
            .load(IMAGES_API_URL + category.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.categoryImage)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int = dataSet.size

}