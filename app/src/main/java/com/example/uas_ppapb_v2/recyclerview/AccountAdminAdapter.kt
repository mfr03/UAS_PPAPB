package com.example.uas_ppapb_v2.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ppapb_v2.databinding.AdminAccountBinding

class AccountAdminAdapter(private val adminList: List<Triple<String, String, String>>) : RecyclerView.Adapter<AccountAdminAdapter.AccountAdminViewHolder>() {

    inner class AccountAdminViewHolder(private val binding: AdminAccountBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Triple<String, String, String>) {
            with(binding) {
                adminUID.text = account.first
                adminName.text = account.second
                adminEmail.text = account.third
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountAdminViewHolder {
        val binding = AdminAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AccountAdminViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    override fun onBindViewHolder(holder: AccountAdminViewHolder, position: Int) {
        holder.bind(adminList[position])
    }
}