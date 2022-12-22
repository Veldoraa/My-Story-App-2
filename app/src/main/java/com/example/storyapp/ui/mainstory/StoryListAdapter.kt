package com.example.storyapp.ui.mainstory

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.local.Entity
import com.example.storyapp.R
import com.example.storyapp.databinding.RvItemBinding

class StoryListAdapter: PagingDataAdapter<Entity, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBindingItem =
            RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBindingItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataStory = getItem(position)
        if (dataStory != null) {
            holder.bind(dataStory)
        }
    }

    class ViewHolder(binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var imgPhoto  = binding.ivImage
        private var tvName = binding.tvName
        private var tvDesc = binding.tvDescription

        fun bind(dataStory: Entity) {
            Glide.with(itemView.context)
                .load(dataStory.photoUrl)
                .placeholder(R.drawable.ic_image)
                .into(imgPhoto)

            tvName.text = dataStory.name
            tvDesc.text = dataStory.description
            itemView.setOnClickListener {

                val detailMove = Intent(itemView.context, StoryDetailActivity::class.java)
                detailMove.putExtra("story", dataStory)
                itemView.context.startActivity(detailMove)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Entity> =
            object : DiffUtil.ItemCallback<Entity>() {
                override fun areItemsTheSame(oldUser: Entity, newUser: Entity): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: Entity,
                    newUser: Entity
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }

}