package com.example.uas_ppapb_v2.tablayout

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uas_ppapb_v2.view.fragment.LoginFragment
import com.example.uas_ppapb_v2.view.fragment.RegisterFragment

class TabAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    private val page = arrayOf(LoginFragment(), RegisterFragment())

    override fun getItemCount(): Int {
        return page.size
    }
    override fun createFragment(position: Int): Fragment {
        return page[position]
    }
    fun getFragment(position: Int): Fragment {
        return page[position]
    }
}