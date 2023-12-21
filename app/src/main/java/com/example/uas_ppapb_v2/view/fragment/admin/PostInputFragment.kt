package com.example.uas_ppapb_v2.view.fragment.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.database.model.Post
import com.example.uas_ppapb_v2.databinding.FragmentPostInputBinding
import com.google.android.material.snackbar.Snackbar


class PostInputFragment : Fragment() {

    private val binding by lazy {
        FragmentPostInputBinding.inflate(layoutInflater)
    }

    private var selectedTag : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fireStoreManager = (requireActivity().application as CustomApp).run {returnFireStoreManager()}
        with(binding) {
            if(arguments?.isEmpty != true) {
                val postId = arguments?.getString("postID")
                if(postId != null) {
                    val post = fireStoreManager.getPost(postId)
                    if(post != null) {
                        textInputEditTextPostInputFragmentDestination.setText(post.destination)
                        textInputEditTextPostInputFragmentStartingStation.setText(post.startingStation)
                        textInputEditTextPostInputFragmentEndStation.setText(post.endStation)
                        textInputEditTextPostInputFragmentShortDescription.setText(post.shortDescription)
                        textInputEditTextPostInputFragmentOverviewDescription.setText(post.overviewDescription)
                        textInputEditTextPostInputFragmentPrice.setText(post.price.toString())
                        textInputEditTextPostInputFragmentDate.setText(post.lengthOfStay.toString())
                        uri.setText(post.imageURI)
                        autoCompletePostInputFragmentTag.setText(post.tag, false)
                        imageView5.load(post.imageURI) {
                            crossfade(true)
                            crossfade(500)
                        }
                    }
                }
            }

            val tags = resources.getStringArray(R.array.tags).toList()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tags)
            autoCompletePostInputFragmentTag.setAdapter(adapter)

            autoCompletePostInputFragmentTag.setOnClickListener {
                autoCompletePostInputFragmentTag.showDropDown()
            }

            autoCompletePostInputFragmentTag.setOnItemClickListener { parent, _, i, _ ->
                selectedTag = parent.getItemAtPosition(i).toString()
            }

            loadImage.setOnClickListener {
                imageView5.load(uri.text.toString()) {
                    crossfade(true)
                    crossfade(500)
                }
            }


            submit.setOnClickListener {
                val post = Post (
                    destination = textInputEditTextPostInputFragmentDestination.text.toString(),
                    startingStation = textInputEditTextPostInputFragmentStartingStation.text.toString(),
                    endStation = textInputEditTextPostInputFragmentEndStation.text.toString(),
                    shortDescription = textInputEditTextPostInputFragmentShortDescription.text.toString(),
                    overviewDescription = textInputEditTextPostInputFragmentOverviewDescription.text.toString(),
                    price = textInputEditTextPostInputFragmentPrice.text.toString().trim().toInt(),
                    lengthOfStay = textInputEditTextPostInputFragmentDate.text.toString().toInt(),
                    imageURI = uri.text.toString(),
                    tag = selectedTag
                )
                if(arguments?.isEmpty != true) {
                    post.id = arguments?.getString("postID").toString()
                    fireStoreManager.updatePost(post, onSuccess = {
                        Snackbar.make(root, "Post updated", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_postInputFragment_to_dashboardAdminFragment)
                    }, onFailed = {
                        Snackbar.make(root, "Failed to be updated", Snackbar.LENGTH_SHORT).show()
                    })
                } else {
                    fireStoreManager.addPost(post, onSuccess = {
                        Snackbar.make(root, "Post added", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_postInputFragment_to_dashboardAdminFragment)
                    }, onFailed = {
                        Snackbar.make(root, "Failed to be added", Snackbar.LENGTH_SHORT).show()
                    })
                }

            }
        }
    }


}