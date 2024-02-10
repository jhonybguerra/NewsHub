package com.jbgcomposer.newshub.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jbgcomposer.newshub.R
import com.jbgcomposer.newshub.databinding.ItemArticleBinding
import com.jbgcomposer.newshub.db.FavoriteArticle
import com.jbgcomposer.newshub.utils.dateFormat

class FavoritesAdapter(
    private val onItemClicked: (FavoriteArticle) -> Unit
) : ListAdapter<FavoriteArticle, FavoritesAdapter.NewsViewHolder>(NewsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: FavoriteArticle) {
            binding.apply {
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvSource.text = article.source
                tvPublishedAt.text = article.publishedAt.dateFormat()

                Glide.with(root)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.ic_image_not_found)
                    .into(ivArticleImage)

                root.setOnClickListener {
                    onItemClicked(article)
                }
            }
        }
    }

    companion object NewsDiffCallback : DiffUtil.ItemCallback<FavoriteArticle>() {
        override fun areItemsTheSame(oldItem: FavoriteArticle, newItem: FavoriteArticle): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: FavoriteArticle, newItem: FavoriteArticle): Boolean {
            return oldItem == newItem
        }
    }
}