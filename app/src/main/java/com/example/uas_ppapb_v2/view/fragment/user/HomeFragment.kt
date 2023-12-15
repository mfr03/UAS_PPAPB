package com.example.uas_ppapb_v2.view.fragment.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.activity.PostActivity
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentHomeBinding
import com.example.uas_ppapb_v2.recyclerview.PostAdapter

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
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

        val fireStoreManager = (requireActivity().application as CustomApp).returnFireStoreManager()
        fireStoreManager.getAllPost()

        with(binding) {
            fireStoreManager.returnPostLiveData().observe(viewLifecycleOwner) {
                post ->
                if (post != null) {
                    popularRV.apply {
                        adapter = PostAdapter(post) {
                            val intent = Intent(context, PostActivity::class.java)
                            intent.putExtra("postID", it.id)
                            startActivity(intent)
                        }
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }

        if((activity as AppCompatActivity).supportActionBar?.isShowing == false) {
            (activity as AppCompatActivity).supportActionBar?.show()
        }

    }
}