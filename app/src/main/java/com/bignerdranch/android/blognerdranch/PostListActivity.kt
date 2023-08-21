package com.bignerdranch.android.blognerdranch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.bignerdranch.android.blognerdranch.rest.BlogService
import com.bignerdranch.android.blognerdranch.databinding.ActivityPostListBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class PostListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPostListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navGraph = supportFragmentManager.findFragmentById(R.id.nav_frag) as NavHostFragment
        setupActionBarWithNavController(navGraph.navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_frag).navigateUp() || super.onSupportNavigateUp()
    }
}
