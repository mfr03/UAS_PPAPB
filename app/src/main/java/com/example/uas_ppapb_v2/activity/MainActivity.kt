package com.example.uas_ppapb_v2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.activity.listener.LoadingListener
import com.example.uas_ppapb_v2.activity.listener.TabLayoutListener
import com.example.uas_ppapb_v2.databinding.ActivityMainBinding
import com.example.uas_ppapb_v2.tablayout.TabAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), TabLayoutListener, LoadingListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tabAdapter = TabAdapter(this)
        with(binding) {
            viewPager.adapter = tabAdapter
            viewPager2 = viewPager
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when(position) {
                    1 -> "Register"
                    0 -> "Login"
                    else -> "Undefined"
                }
            }.attach()
        }
    }

    override fun goToNextFragment(item: Int) {
        viewPager2.setCurrentItem(item, true)
    }

    override fun showLoadingScreen() {
        binding.progressBar2.visibility = View.VISIBLE
        binding.loadingOverlay.visibility = View.VISIBLE
    }

    override fun hideLoadingScreen() {
        binding.progressBar2.visibility = View.GONE
        binding.loadingOverlay.visibility = View.GONE
    }
}