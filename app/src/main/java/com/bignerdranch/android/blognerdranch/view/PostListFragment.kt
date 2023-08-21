package com.bignerdranch.android.blognerdranch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.blognerdranch.R
import com.bignerdranch.android.blognerdranch.view.adapter.PostAdapter
import com.bignerdranch.android.blognerdranch.databinding.FragmentPostListBinding
import com.bignerdranch.android.blognerdranch.utils.BaseFragment
import com.bignerdranch.android.blognerdranch.utils.POST_TITLE_ARG
import com.bignerdranch.android.blognerdranch.utils.State
import kotlinx.coroutines.launch

class PostListFragment : BaseFragment() {

    private val binding by lazy {
        FragmentPostListBinding.inflate(layoutInflater)
    }

    private val postAdapter by lazy {
        PostAdapter {
            pViewModel.selectedPost = it
            findNavController().navigate(
                R.id.action_PostListFrag_to_PostDetailsFrag,
                bundleOf(Pair(POST_TITLE_ARG, it.title ?: " Post details"))
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.postRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        lifecycleScope.launch {
            pViewModel.posts.collect { state ->
                when(state) {
                    State.LOADING -> {
                        binding.loadState.visibility = View.VISIBLE
                        binding.postRecycler.visibility = View.GONE
                    }
                    is State.SUCCESS -> {
                        binding.loadState.visibility = View.GONE
                        binding.postRecycler.visibility = View.VISIBLE
                        postAdapter.updateNewPosts(state.data)
                    }
                    is State.ERROR -> {
                        binding.loadState.visibility = View.GONE
                        showError(state.error.localizedMessage) {
                            pViewModel.retryGetPosts()
                        }
                    }
                }
            }
        }

        return binding.root
    }
}