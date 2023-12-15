package com.example.uas_ppapb_v2.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.uas_ppapb_v2.database.model.FavoritePost
import com.example.uas_ppapb_v2.databinding.PostCardBinding

class FavoritePostAdapter (private val postList: List<FavoritePost>, private val onItemClicked: (FavoritePost) -> Unit)
    : RecyclerView.Adapter<FavoritePostAdapter.PostViewHolder>() {
    inner class PostViewHolder(private val binding: PostCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: FavoritePost) {
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