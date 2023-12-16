package com.example.uas_ppapb_v2.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.uas_ppapb_v2.database.model.PlannedPost
import com.example.uas_ppapb_v2.databinding.PlannedPostBinding

class PlannedPostAdapter(private val postList: List<PlannedPost>, private val onDelete: (PlannedPost) -> Unit): RecyclerView.Adapter<PlannedPostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: PlannedPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PlannedPost) {
            with(binding) {
                location.text = post.destination
                shortDescription.text = post.shortDescription

                imageView2.load(post.imageURI) {
                    placeholder(imageView2.drawable)
                    error(imageView2.drawable)
                }

                dateAndTime.text = "Date: ${post.plannedDate} Time: ${post.plannedTime}"

                deleteButton.setOnClickListener {
                    onDelete(post)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PlannedPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }
}