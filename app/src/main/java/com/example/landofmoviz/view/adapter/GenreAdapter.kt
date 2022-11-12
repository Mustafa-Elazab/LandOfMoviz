package com.example.landofmoviz.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.landofmoviz.R
import com.example.landofmoviz.databinding.ItemGenreBinding
import com.example.landofmoviz.domain.model.Genre
import com.example.landofmoviz.utils.MediaType

class GenreAdapter(private val mediaType: MediaType) : ListAdapter<Pair<Int, String>, GenreAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(val view: ItemGenreBinding) : RecyclerView.ViewHolder(view.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_genre, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.apply {
            mediaType = this@GenreAdapter.mediaType
            genre = Genre(id = getItem(position).first, name = getItem(position).second)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Pair<Int, String>>() {
            override fun areItemsTheSame(oldItem: Pair<Int, String>, newItem: Pair<Int, String>): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(oldItem: Pair<Int, String>, newItem: Pair<Int, String>): Boolean {
                return oldItem == newItem
            }
        }
    }
}