package com.example.uas_ppapb_v2.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.uas_ppapb_v2.database.model.Post
import com.example.uas_ppapb_v2.databinding.PostCardBinding

class PostAdapter (private val postList: List<Post>, private val onItemClicked: (Post) -> Unit): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    inner class PostViewHolder(private val binding: PostCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            with(binding) {
                destination.text = post.destination
                shortdesc.text = post.shortDescription

                destImage.load(post.imageURI) {
                    placeholder(destImage.drawable)
                    error(destImage.drawable)
                }

                root.setOnClickListener {
                    onItemClicked(post)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(
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