package com.bignerdranch.android.blognerdranch.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.databinding.PostItemBinding
import com.bignerdranch.android.blognerdranch.utils.parseDisplayDate

class PostViewHolder(
    private val binding: PostItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(postMetadata: PostMetadata, onClickPost: (PostMetadata) -> Unit) {
        binding.postAuthor.text = postMetadata.author?.name ?: "N/A"
        binding.postSummary.text = postMetadata.summary ?: "N/A"
        binding.postTitle.text = postMetadata.title ?: "N/A"
        binding.postDate.text = postMetadata.publishDate?.parseDisplayDate()

        binding.root.setOnClickListener { onClickPost(postMetadata) }
    }
}