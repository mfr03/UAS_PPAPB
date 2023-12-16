package com.example.uas_ppapb_v2.view.fragment.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppapb_v2.activity.PostActivity
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentHistoryBinding
import com.example.uas_ppapb_v2.recyclerview.FavoritePostAdapter
import com.example.uas_ppapb_v2.recyclerview.PlannedPostAdapter


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

            roomDBManager.getPlannedPosts().observe(viewLifecycleOwner) {plans ->
                if(plans.isEmpty()) {
                    plannedRV.visibility = View.GONE
                } else {
                    plannedRV.visibility = View.VISIBLE
                    plannedRV.apply {
                        adapter = PlannedPostAdapter(plans) {
                            val conformationDialog = ConformationDialog {
                                roomDBManager.deletePlannedPost(it)
                            }
                            conformationDialog.show(parentFragmentManager, "delete")
                        }
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
        }
    }
}