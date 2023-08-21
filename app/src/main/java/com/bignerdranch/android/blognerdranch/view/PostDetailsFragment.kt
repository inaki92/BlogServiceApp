package com.bignerdranch.android.blognerdranch.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bignerdranch.android.blognerdranch.R
import com.bignerdranch.android.blognerdranch.databinding.FragmentPostDetailsBinding
import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.utils.BaseFragment
import com.bignerdranch.android.blognerdranch.utils.State
import com.bignerdranch.android.blognerdranch.utils.parseDisplayDate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PostDetailsFragment : BaseFragment() {

    private val binding by lazy {
        FragmentPostDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        lifecycleScope.launch {
            pViewModel.postMetadata.collect { state ->
                when(state) {
                    State.LOADING -> {
                        binding.loadDetails.visibility = View.VISIBLE
                        binding.detailsPost.visibility = View.GONE
                    }
                    is State.SUCCESS -> {
                        updateUI(state.data)
                        binding.loadDetails.visibility = View.GONE
                        binding.detailsPost.visibility = View.VISIBLE
                    }
                    is State.ERROR ->  {
                        binding.loadDetails.visibility = View.GONE
                        binding.detailsPost.visibility = View.GONE

                        showError(state.error.localizedMessage) {

                        }
                    }
                }
            }
        }

        pViewModel.getPostDetails()

        return binding.root
    }

    private fun updateUI(post: Post) {
        binding.postTitle.text = post.metadata?.title ?: "N/A"
        binding.postAuthor.text = post.metadata?.author?.name ?: "N/A"
        binding.postDate.text = post.metadata?.publishDate.parseDisplayDate()
        binding.postBody.text = post.body ?: "N/A"
    }
}