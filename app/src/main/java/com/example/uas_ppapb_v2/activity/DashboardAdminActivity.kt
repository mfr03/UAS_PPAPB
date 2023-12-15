package com.example.uas_ppapb_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.databinding.ActivityDashboardAdminBinding

class DashboardAdminActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDashboardAdminBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val navController = findNavController(R.id.nav_host_fragment_admin)
            bottomNavigationViewAdmin.setupWithNavController(navController)
        }
    }
}