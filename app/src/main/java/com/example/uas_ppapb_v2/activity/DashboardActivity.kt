package com.example.uas_ppapb_v2.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigationView.setupWithNavController(navController)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}