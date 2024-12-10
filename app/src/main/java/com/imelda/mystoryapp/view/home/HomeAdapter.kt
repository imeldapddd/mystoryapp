package com.imelda.mystoryapp.view.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imelda.mystoryapp.R
import com.imelda.mystoryapp.data.response.ListStoryItem
import com.imelda.mystoryapp.databinding.ItemLayoutBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val stories = mutableListOf<ListStoryItem>()

    fun submitList(newStories: List<ListStoryItem>) {
        stories.clear()
        stories.addAll(newStories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.textJudul.text = story.name
            binding.textDeskripsi.text = story.description

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_image)
                .into(binding.gambar)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("STORY_NAME", story.name)
                    putExtra("STORY_DESCRIPTION", story.description)
                    putExtra("STORY_PHOTO_URL", story.photoUrl)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}