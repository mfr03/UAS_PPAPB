package com.example.uas_ppapb_v2.view.fragment.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.activity.PostActivity
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentHistoryBinding
import com.example.uas_ppapb_v2.recyclerview.FavoritePostAdapter
import com.example.uas_ppapb_v2.recyclerview.PlannedPostAdapter
import com.example.uas_ppapb_v2.view.fragment.user.dialog.ConformationDialog


class HistoryFragment : Fragment() {

    private val binding: FragmentHistoryBinding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }

    private val navOptions: NavOptions by lazy {
        NavOptions.Builder()
            .setEnterAnim(com.google.android.material.R.anim.m3_side_sheet_enter_from_right)
            .setExitAnim(com.google.android.material.R.anim.m3_side_sheet_exit_to_left)
            .setPopUpTo(R.id.historyFragment, false)
            .build()
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
                    textView11.visibility = View.GONE
                    favoriteRV.visibility = View.GONE
                    emptyFavoriteList.visibility = View.VISIBLE
                    addFavorite.visibility = View.VISIBLE
                    addFavorite.setOnClickListener {
                        findNavController().navigate(R.id.action_historyFragment_to_homeFragment, null, navOptions)
                    }
                } else {
                    favoriteRV.visibility = View.VISIBLE
                    textView11.visibility = View.VISIBLE
                    emptyFavoriteList.visibility = View.GONE
                    addFavorite.visibility = View.GONE
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
                    textView10.visibility = View.GONE
                    emptyTextPlan.visibility = View.VISIBLE
                    makePlanButton.visibility = View.VISIBLE
                    makePlanButton.setOnClickListener {
                        findNavController().navigate(R.id.action_historyFragment_to_homeFragment, null, navOptions)
                    }
                } else {
                    plannedRV.visibility = View.VISIBLE
                    textView10.visibility = View.VISIBLE
                    emptyTextPlan.visibility = View.GONE
                    makePlanButton.visibility = View.GONE
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