package com.bignerdranch.android.blognerdranch.utils

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.blognerdranch.viewmodel.PostViewModel

open class BaseFragment : Fragment() {

    protected val pViewModel by lazy {
        ViewModelProvider(requireActivity())[PostViewModel::class.java]
    }

    fun showError(message: String? = "Unknown error", action: () -> Unit) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Error occurred")
            .setMessage(message)
            .setPositiveButton("Retry") { dialog, _ ->
                action()
                dialog.dismiss()
            }
            .setNegativeButton("Dismiss") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}