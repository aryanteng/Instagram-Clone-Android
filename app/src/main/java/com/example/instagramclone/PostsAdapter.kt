package com.example.instagramclone

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.models.Post

class PostsAdapter (private val context: Context, private val posts: List<Post>):
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    private lateinit var tvUsername: TextView
    private lateinit var tvDesc: TextView
    private lateinit var ivImgSrc: ImageView
    private lateinit var tvCreatedAt: TextView

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(post: Post){
            tvUsername.text = post.user?.username
            tvDesc.text = post.description
            Glide.with(context).load(post.imgSrc).into(ivImgSrc)
            tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(post.createdAt)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        tvUsername = view.findViewById(R.id.username)
        tvDesc = view.findViewById(R.id.desc)
        ivImgSrc = view.findViewById(R.id.img_src)
        tvCreatedAt = view.findViewById(R.id.created_at)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }
}