package com.bignerdranch.android.blognerdranch.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bignerdranch.android.blognerdranch.databinding.PostItemBinding
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.view.adapter.PostViewHolder

class PostAdapter(
    private val postMetadata: MutableList<PostMetadata> = mutableListOf(),
    private val onPostClick: (PostMetadata) -> Unit
) : RecyclerView.Adapter<PostViewHolder>() {

    override fun getItemCount(): Int = postMetadata.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postMetadata[position]
        holder.bind(post, onPostClick)
    }

    fun updateNewPosts(data: List<PostMetadata>) {
        postMetadata.clear()
        postMetadata.addAll(data)
        notifyDataSetChanged()
    }

}