package com.example.uas_ppapb_v2.view.fragment.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentAddAdminBinding
import com.example.uas_ppapb_v2.recyclerview.AccountAdminAdapter
import com.google.android.material.snackbar.Snackbar


class AddAdminFragment : Fragment() {

    private val binding: FragmentAddAdminBinding by lazy {
        FragmentAddAdminBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authManager = (requireActivity().application as CustomApp).getAuthManager()
        with(binding) {
            addAdmin.setOnClickListener {
                val bottomSheetDialog = RegisterAdminDialog {
                    Snackbar.make(binding.root, "Register Admin Success", Snackbar.LENGTH_SHORT).show()

                    authManager.getAdmins { data->
                        if (data != null) {
                            data.observe(viewLifecycleOwner) {
                                Log.d("AddAdminFragment", "onViewCreated: $it")
                                if (it != null) {
                                    rvadd.apply {
                                        adapter = AccountAdminAdapter(it)
                                        layoutManager = LinearLayoutManager(requireContext())
                                    }

                                }
                            }
                        }
                    }
                }
                bottomSheetDialog.show(parentFragmentManager, "RegisterAdminDialog")
            }
            authManager.getAdmins { data->
                if (data != null) {
                    data.observe(viewLifecycleOwner) {
                        Log.d("AddAdminFragment", "onViewCreated: $it")
                        if (it != null) {
                            rvadd.apply {
                                adapter = AccountAdminAdapter(it)
                                layoutManager = LinearLayoutManager(requireContext())
                            }

                        }
                    }
                }
            }
        }
    }



}