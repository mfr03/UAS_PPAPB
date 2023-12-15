package com.example.uas_ppapb_v2.view.fragment.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.activity.PostActivity
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentHistoryBinding
import com.example.uas_ppapb_v2.recyclerview.FavoritePostAdapter


class HistoryFragment : Fragment() {

    private val binding: FragmentHistoryBinding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val roomDBManager = (requireActivity().application as CustomApp).getRoomDBManager()

        with(binding) {
            roomDBManager.getFavorites().observe(viewLifecycleOwner) {
                if(it.isEmpty()) {
                    favoriteRV.visibility = View.GONE
                    recentViewedRV.visibility = View.GONE
                } else {
                    favoriteRV.apply {
                        adapter = FavoritePostAdapter(it) {
                            val intent = Intent(context, PostActivity::class.java)
                            intent.putExtra("postID", it.id)
                            startActivity(intent)
                        }
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }
    }
}