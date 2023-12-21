package com.example.uas_ppapb_v2.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.uas_ppapb_v2.activity.DashboardActivity
import com.example.uas_ppapb_v2.activity.DashboardAdminActivity
import com.example.uas_ppapb_v2.activity.listener.LoadingListener
import com.example.uas_ppapb_v2.activity.listener.TabLayoutListener
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
            spannableClick(forgotPassword, "Forgot Password?")
            spannableClick(alreadyAccount, "Register", 1)

            loginButton.setOnClickListener {
                val email = inputLoginEmail.text.toString()
                val password = inputPassword.text.toString()
                if(email.isEmpty() || password.isEmpty()) {
                    Snackbar.make(view, "Email dan password tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
                } else {
                    (requireActivity() as LoadingListener).showLoadingScreen()
                    auth.loginAccount(email, password,
                        onSuccess = { isAdmin ->
                            if(isAdmin) {
                                auth.getAccount()
                                auth.setLoggedIn(true, isAdmin = true)
                                (requireActivity() as LoadingListener).hideLoadingScreen()
                                val intent = Intent(requireContext(), DashboardAdminActivity::class.java)
                                startActivity(intent)
                                clearInput()
                            } else {
                                auth.getAccount()
                                auth.setLoggedIn(true, isAdmin = false)
                                (requireActivity() as LoadingListener).hideLoadingScreen()
                                val intent = Intent(requireContext(), DashboardActivity::class.java)
                                startActivity(intent)
                                clearInput()
                            }
                        }, onFailed = {
                            (requireActivity() as LoadingListener).hideLoadingScreen()
                            Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        })
                }
            }
        }
    }

    private fun clearInput() {
        binding.inputLoginEmail.text!!.clear()
        binding.inputPassword.text!!.clear()
    }

    private fun spannableClick(tv: TextView, clickString: String, next: Int? = null) {
        val spannableString = SpannableString(tv.text)
        val clickableSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                if(clickString == "Forgot Password?") {
                    Snackbar.make(binding.root, "good luck scrub", Snackbar.LENGTH_SHORT).show()
                } else {
                    (requireActivity() as TabLayoutListener).goToNextFragment(next!!)
                }
            }
        }

        val int1 = tv.text.toString().indexOf(clickString)
        val int2 = int1 + clickString.length
        spannableString.setSpan(clickableSpan, int1, int2, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        tv.text = spannableString
        tv.movementMethod = LinkMovementMethod.getInstance()
    }
}