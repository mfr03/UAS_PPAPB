package com.example.uas_ppapb_v2.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.uas_ppapb_v2.database.model.Post
import com.example.uas_ppapb_v2.databinding.AdminPostCardBinding

class AdminPostAdapter (private val postList: List<Post>, private val onEdit: (Post) -> Unit, private val onDelete: (Post) -> Unit): RecyclerView.Adapter<AdminPostAdapter.PostViewHolder>() {
    inner class PostViewHolder(private val binding: AdminPostCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            with(binding) {
                destination.text = post.destination
                shortdesc.text = post.shortDescription

                editPost.setOnClickListener {
                    onEdit(post)
                }
                deletePost.setOnClickListener {
                    onDelete(post)
                }

                imageView3.load(post.imageURI) {
                    crossfade(true)
                    crossfade(1000)
                    placeholder(imageView3.drawable)
                    error(imageView3.drawable)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = AdminPostCardBinding.inflate(
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