package com.example.uas_ppapb_v2.view.fragment.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {


    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
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

        val auth = (requireActivity().application as CustomApp).getAuthManager()


        with(binding) {

            with(auth.returnAccount()) {
                username2.text = username
                email2.text = email
                dob2.text = dateOfBirth
                nim2.text = nim
            }
            logout.setOnClickListener {
                auth.logout()
                requireActivity().finish()
            }
        }
    }

}