package com.example.uas_ppapb_v2.view.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentHomeAdminBinding
import com.example.uas_ppapb_v2.recyclerview.AdminPostAdapter
import com.example.uas_ppapb_v2.view.fragment.user.ConformationDialog
import com.google.android.material.snackbar.Snackbar

class HomeAdminFragment : Fragment() {

    private val binding by lazy {
        FragmentHomeAdminBinding.inflate(layoutInflater)
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
        val fireStoreManager = (requireActivity().application as CustomApp).run {returnFireStoreManager()}
        fireStoreManager.getAllPost()
        with(binding) {
            fireStoreManager.returnPostLiveData().observe(viewLifecycleOwner) {
                if (it != null) {
                    homeAdminRV.visibility = View.VISIBLE
                    homeAdminRV.apply {
                        adapter = AdminPostAdapter(it,
                            onEdit = {post ->
                                val bundle = Bundle()
                                bundle.putString("postID", post.id)
                                findNavController().navigate(R.id.action_homeAdminFragment_to_postInputFragment, bundle)
                            },
                            onDelete = {post ->
                                val conformationDialog = ConformationDialog {
                                    fireStoreManager.deletePost(post,
                                        onSuccess = {
                                            Snackbar.make(binding.root, "Post Deleted", Snackbar.LENGTH_SHORT).show()
                                        },
                                        onFailed = {Snackbar.make(binding.root, "Failed to be deleted", Snackbar.LENGTH_SHORT).show()})
                                }
                                conformationDialog.show(parentFragmentManager, "ConformationDialog")
                            }
                        )
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                } else {
                    homeAdminRV.visibility = View.GONE
                }
            }
        }

    }

}