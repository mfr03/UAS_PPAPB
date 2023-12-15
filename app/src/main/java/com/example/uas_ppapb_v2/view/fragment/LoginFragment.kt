package com.example.uas_ppapb_v2.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uas_ppapb_v2.activity.DashboardActivity
import com.example.uas_ppapb_v2.activity.DashboardAdminActivity
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {


    private val binding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = (requireActivity().application as CustomApp).run { getAuthManager() }

        if(auth.isLoggedIn()) {
            if(auth.isAdmin()) {
                val intent = Intent(requireContext(), DashboardAdminActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intent)
            }
        }

        with(binding) {

            loginButton.setOnClickListener {

                val email = inputLoginEmail.text.toString()
                val password = inputPassword.text.toString()
                if(email.isEmpty() || password.isEmpty()) {
                    Snackbar.make(view, "Email dan password tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
                } else {
                    auth.loginAccount(email, password,
                        onSuccess = { isAdmin ->
                            if(isAdmin) {
                                auth.getAccount()
                                auth.setLoggedIn(true, isAdmin = true)
                                val intent = Intent(requireContext(), DashboardAdminActivity::class.java)
                                startActivity(intent)
                            } else {
                                auth.getAccount()
                                auth.setLoggedIn(true, isAdmin = false)
                                val intent = Intent(requireContext(), DashboardActivity::class.java)
                                startActivity(intent)
                            }
                        }, onFailed = {
                            Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        })
                }
            }
        }


    }
}