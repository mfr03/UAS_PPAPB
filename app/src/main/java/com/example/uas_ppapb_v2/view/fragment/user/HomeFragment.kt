package com.example.uas_ppapb_v2.view.fragment.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.uas_ppapb_v2.database.model.Post
import com.example.uas_ppapb_v2.databinding.FragmentHomeBinding
import com.example.uas_ppapb_v2.recyclerview.PostAdapter

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val fireStoreManager by lazy {
        (requireActivity().application as CustomApp).returnFireStoreManager()
    }
    private var allowedPost: List<Post>? = null
    private var currentTag: String? = null
    private val tags = resources.getStringArray(R.array.tags).toList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fireStoreManager.getAllPost()
        with(binding) {

            stayTab.setOnClickListener {
                currentTag = tags[0]
                observe()
            }
            activitiesTab.setOnClickListener {
                currentTag = tags[1]
                observe()
            }
            toursTab.setOnClickListener {
                currentTag = tags[2]
                observe()
            }
            popularTab.setOnClickListener {
                currentTag = tags[3]
                observe()
            }
            observe()
        }

        if((activity as AppCompatActivity).supportActionBar?.isShowing == false) {
            (activity as AppCompatActivity).supportActionBar?.show()
        }
    }



    private fun observe() {
        with(binding){
            fireStoreManager.returnPostLiveData().observe(viewLifecycleOwner) {
                    post ->
                if (post != null) {

                    when(currentTag) {
                        "Stays" -> {
                            allowedPost = post.filter { item ->
                                item.tag == tags[0]
                            }
                        }
                        "Activities" -> {
                            allowedPost = post.filter { item ->
                                item.tag == tags[1]
                            }
                        }
                        "Tours" -> {
                            allowedPost = post.filter { item ->
                                item.tag == tags[2]
                            }
                        }
                        else -> {
                            allowedPost = post
                        }
                    }

                    if (currentTag == null){
                        textView3.text = "Popular Destinations"
                    } else {
                        textView3.text = "$currentTag Destinations"
                    }

                    popularRV.apply {
                        adapter = PostAdapter(allowedPost!!) {
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